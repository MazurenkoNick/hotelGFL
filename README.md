# Підсумкове завдання
## [Link to the front-end part](https://github.com/MazurenkoNick/hotelGFL-fe)

## WEB-застосунок із використанням засобів БД: "Застосунок для адміністраторів готелю".
### Реалізувати:
* Список номерів: клас, кількість місць. Список гостей: паспортні дані, дати приїзду та
від’їзду, номер.
* Поселення гостей: вибір відповідного номера (за наявності вільних місць), реєстрація,
оформлення квитанції.
* Від’їзд: вибір всіх гостей, які від’їжджають сьогодні, звільнення місця або оформлення
затримки з випискою додаткової квитанції. Можливість дострокового від’їзду з
перерахунком.
* Пошук гостя за довільною ознакою.

### Хід виконання роботи
![image](https://github.com/MazurenkoNick/hotelGFL/assets/104276704/8f9fb012-34dc-40e3-9333-a80d9102960c)

Побудовано базу даних, на основі якої будуть створені сутності: Administrator, Discount, Receipt, Renter, Role, Room, RoomClass.
Для сутностей Administrator та Renter було створено абстрактний клас User для того, щоб не повтороювати властивості в кожній сутності:
```java
@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "passport_id", nullable = false, unique = true)
    private String passportId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_num", nullable = false, unique = true)
    private String phoneNumber;

    public User(String firstName, String lastName, String passportId, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}

```
пр. наслідування Administrator:
```java
@Entity
@Table(name = "administrators")
@NoArgsConstructor
@Getter
@Setter
public class Administrator extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_rank", nullable = false)
    private Rank rank;

    @Column(name = "salary")
    private double salary;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "administrator",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Reservation> reservations;

    @ManyToMany
    @JoinTable(
            name = "admin_roles",
            joinColumns = @JoinColumn(name = "administrator_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public Administrator(String firstName, String lastName, String passportId, String email,String phoneNumber,
                         Rank rank, double salary, String password, List<Reservation> reservations) {

        super(firstName, lastName, passportId, email, phoneNumber);
        this.rank = rank;
        this.salary = salary;
        this.password = password;
        this.reservations = reservations;
    }

    public Administrator(String firstName, String lastName, String passportId, String email,
                         String phoneNumber, Rank rank, double salary, String password) {

        super(firstName, lastName, passportId, email, phoneNumber);
        this.rank = rank;
        this.salary = salary;
        this.password = password;
    }

    public void addReservation(Reservation reservation) {
        Administrator currentAdmin = reservation.getAdministrator();
        if (currentAdmin != null && currentAdmin != this) {
            throw new IllegalArgumentException("Reservation already has an administrator!");
        }
        reservation.setAdministrator(this);
        reservations.add(reservation);
    }

    ...
}
```
У вищевказаному класі також створено додатковий метод `addReservation`, що буде зручно використовувати під час каскадного додавання.
Схожі методи є і в інших сутностях.

### Persistence Layer
Було реалізовано наступні репозиторії: AdministratorRepository, DiscountRepository, ReceiptRepository, RenterRepository, ReservationRepository, RoomClassRepository та
RoomRepository. Майже в усіх класах реалізовані додаткові методи для потреб сервісного рівня та унеможливлення появ n+1 проблем. В деяких класах реалізовано методи з динамічною проекцією для DTO.

Пр. AdministratorRepository:
```java
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    @Query("SELECT DISTINCT a FROM Administrator a LEFT JOIN FETCH a.roles r WHERE a.email = :email")
    Optional<Administrator> findByEmailFetchRoles(@Param("email") String email);

    <T> Optional<T> findByEmail(String email, Class<T> type);

    <T> List<T> findAllBy(Class<T> type);
}
```
Більшість методів буде згадана під час розбору сервісного рівня.
### Service Layer
#### AdministratorService має публічні методи:
* `AdministratorDto create(AdministratorDto administratorDto)` - перетворює dto в сутність за допомогою бібліотеки MapStruct, потім хешує пароль з dto за допомогою `PasswordEncoder`. Після, зберігає новостворену сутність в бд.
```java
@Transactional
public AdministratorDto create(AdministratorDto administratorDto) {
    Administrator admin = administratorMapper.dtoToEntity(administratorDto);
    String hashPassword = passwordEncoder.encode(administratorDto.getPassword());
    admin.setPassword(hashPassword);
    administratorRepository.save(admin);
    return administratorMapper.entityToDto(admin);
}
```
* `AdministratorDto delete(HttpSession session, String email)` - починається з перевірки, чи поточний аутентифікований користувач (Authentication об'єкт з SecurityContext) має email (name) такий самий, як і в аргументі. Якщо
  так, то буде викликаний метод `session.invalidate()` задля того, щоб користувач не зміг продовжити користування застосунком, якщо він вже є видаленим. Після, йде пошук в бд адміністратора з email'ом з аргументу і його видалення.
```java
@Transactional
public AdministratorDto delete(HttpSession session, String email) {
    if (getEmailFromSecurityContext().equals(email)) {
        session.invalidate();
    }
    Administrator administrator = administratorRepository.findByEmail(email, Administrator.class)
            .orElseThrow(EntityNotFoundException::new);
    administratorRepository.delete(administrator);
    return administratorMapper.entityToDto(administrator);
}
```
* `AdministratorDto update(String email, UpdateAdministratorDto administratorDto)` - починається з пошуку адміністратора в бд за допомогою аргументу `email`. Після, якщо поточний аутентифікований користувач
  (Authentication об'єкт з SecurityContext) має email (name) такий самий, як і в аргументі, то потрібно оновити цей email (name) Authentication об'єкту, щоб у наступних запитах користувача він був коректним.
  Заробітна плата та ранг адміністратора оновлюються тільки якщо аутентифікований користувач (адміністратор) має роль "ADMIN".
   Потім, йде звичайне оновлення полів адміністратора за допомогою dto. Оновлення сутності відбувається через Dirty Checking.
```java
@Transactional
public AdministratorDto update(String email, UpdateAdministratorDto administratorDto) {
    Administrator administrator = administratorRepository.findByEmail(email, Administrator.class)
            .orElseThrow(EntityNotFoundException::new);

    // if the user who updates the administrator and the administrator are the same people,
    // then update authenticated email using dto value
    String authenticatedEmail = getEmailFromSecurityContext();
    if (email.equals(authenticatedEmail)) {
        String newAuthenticatedEmail = administratorDto.getEmail();
        updateAuthenticationUsername(newAuthenticatedEmail);
    }
    updateRankAndSalary(administrator, administratorDto);
    administrator.setFirstName(administratorDto.getFirstName());
    administrator.setLastName(administratorDto.getLastName());
    administrator.setEmail(administratorDto.getEmail());
    administrator.setPassportId(administratorDto.getPassportId());
    administrator.setPhoneNumber(administratorDto.getPhoneNumber());

    return administratorMapper.entityToDto(administrator);
}
```
* `<T> T get(String email, Class<T> dtoType)` - повертає дто, або сутність адміністратора за email.
```java
public <T> T get(String email, Class<T> dtoType) {
    return administratorRepository.findByEmail(email, dtoType)
            .orElseThrow(EntityNotFoundException::new);
}
```
* `Administrator get(String email)` - повертає сутність адміністратора за email.
```java
public Administrator get(String email) {
    return get(email, Administrator.class);
}
```
* `<T> List<T> getAll(Class<T> dtoType)` - повертає всі дто, або сутності адміністратора.
```java
public <T> List<T> getAll(Class<T> dtoType) {
    return administratorRepository.findAllBy(dtoType);
}
```
#### RoomService має публічні методи:
* `RoomDto create(RoomDto roomDto)` - створення сутності Room та збереження її в бд за допомогою аргументу roomDto.
* `RoomDto remove(Long roomNumber)` - видалення кімнати з бд за номером кімнати з аргументу.
* `RoomDto update(Long roomNumber, RoomDto roomDto)` - оновлення сутності по id за даними з dto. 
* `RoomDto getDto(Long roomNumber)` - повернення dto кімнати за номером кімнати з аргументу. 
* `Room get(Long roomNumber)` - повернення сутності кімнати за номером кімнати з аргументу.
* `List<RoomDto> getAll()` - повернення всіх dto кімнати з бд.
* `List<RoomDto> getAllFree()` - повернення всіх вільних dto кімнати з бд станом на поточний день.
Логіка запиту вільних dto кімнат станом на поточний день в RoomRepository:
```java
@Query("SELECT new com.example.hotelgfl.dto.RoomDto(" +
        "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
        "FROM Room r " +
        "WHERE NOT EXISTS (" +
        "    SELECT 1 " +
        "    FROM Reservation res " +
        "    WHERE res.room = r " +
        "    AND (DATE(res.fromDateTime) <= CURRENT DATE AND DATE(res.toDateTime) >= CURRENT DATE))"
)
List<RoomDto> findAllFreeRoomDtos();
```
* `List<RoomDto> getAllFree(LocalDate from, LocalDate to)` - повернення всіх вільних dto кімнати з бд на інтервалі `from` та `to` з параметрів методу.
Логіка запиту вільних dto кімнат за інтервалом в RoomRepository:
```java
@Query("SELECT new com.example.hotelgfl.dto.RoomDto(" +
        "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
        "FROM Room r " +
        "WHERE NOT EXISTS (" +
        "    SELECT 1 " +
        "    FROM Reservation res " +
        "    WHERE res.room = r " +
        "    AND (DATE(res.fromDateTime) <= :to AND DATE(res.toDateTime) >= :from))"
)
List<RoomDto> findAllFreeRoomDtos(LocalDate from, LocalDate to);
```
* `boolean isFree(Long roomNumber, LocalDate from, LocalDate to)` - перевіряє, чи доступна кімната за номером на інтервалі з параметру метода.
* `boolean isFreeUpdate(Long reservationId, Long roomNumber, LocalDate from, LocalDate to)` - перевіряє, чи доступна кімната для оновлення дат резервації
  (не враховує поточну резервацію з id = reservationId під час перевірки на доступ по датах).
Логіка запиту:
```java
@Query("SELECT new com.example.hotelgfl.dto.RoomDto(" +
        "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
        "FROM Room r " +
        "WHERE r.roomNumber = :roomNumber " +
        "AND NOT EXISTS (" +
        "    SELECT 1 " +
        "    FROM Reservation res " +
        "    WHERE res.room = r " +
        "    AND res.id != :reservationId " +
        "    AND (DATE(res.fromDateTime) <= :to AND DATE(res.toDateTime) >= :from))")
Optional<RoomDto> findRoomIfFreeUpdate(Long reservationId, Long roomNumber, LocalDate from, LocalDate to);
```
#### ReservationService має публічні методи:
* `ReservationResponseDto create(ReservationDto reservationDto)` - створює нову резервацію, якщо кімната з reservationDto є вільна на проміжку, який також вказаний в reservationDto.
  До резервації привласнюється адміністратор шляхом діставанням його з бд за допомогою email'у з SecurityContext'у.
```java
@Transactional // todo: think about blocking insertion during update and update during insertion of the reservation
public ReservationResponseDto create(ReservationDto reservationDto) {
    Renter renter = renterService.get(reservationDto.getRenterEmail());
    Room room = roomService.get(reservationDto.getRoomNumber());
    Administrator administrator = administratorService.get(getAuthentication().getName());
    LocalDateTime from = reservationDto.getFrom();
    LocalDateTime to = reservationDto.getTo();

    if (roomService.isFree(room.getRoomNumber(), from.toLocalDate(), to.toLocalDate())) {
        Reservation reservation = reservationMapper.dtoToEntity(reservationDto);
        reservation.setAdministrator(administrator);
        reservation.setRoom(room);
        reservation.setRenter(renter);
        reservationRepository.save(reservation);

        return reservationMapper.entityToResponseDto(reservation);
    }
    throw new IllegalArgumentException("The room is not available during the given dates, " +
            "room number: " + room.getRoomNumber() + ", from: " + from + ", to: " + to);
}
```
* `ReservationResponseDto update(Long id, ReservationUpdateDto reservationDto)` - метод має на меті оновлення дат резервації, або/та заміну Renter об'єкту резервації, якщо renterEmail з reservationDto
  є відмінним від email'у поточного Renter'a. Можливість оновлення дат відбувається за допомогою вищеописаного методу `RoomService#isFreeUpdate`.
```java
@Transactional // todo: think about blocking insertion during update and update during insertion of the reservation
public ReservationResponseDto update(Long id, ReservationUpdateDto reservationDto) {
    Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    assertUpdatable(reservation);

    updateReservationRenter(reservation, reservationDto.getRenterEmail());
    updateReservationDates(reservation, reservationDto.getFrom(), reservationDto.getTo());

    return reservationMapper.entityToResponseDto(reservation);
}
```
* `ReceiptResponse checkout(Long id)` - закінчує резервацію, додаючи чек (Receipt до неї). Якщо резервація вже має чек, то її оновлення або checkout призведе до помилки.
```java
@Transactional
public ReceiptResponse checkout(Long id) {
    return checkout(id, null);
}
```
* `ReceiptResponse checkout(Long id, LocalDateTime checkoutDateTime)` - закінчує резервацію, додаючи чек (Receipt до неї). Параметр `checkoutDateTime` використовується у випадку, якщо виїзд відбувається
  раніше, ніж було попередньо в резервації. Також в цьому методі відбувається розрахунок остаточної вартості резервації для клієнта. Якщо клієнт має знижку для поточного класу кімнати, то вона буде використана.
  Метод `findByIdFetchDiscounts` був використаний задля запобігання n+1 проблеми під час пошуку знижки для користувача.
```java
@Transactional
public ReceiptResponse checkout(Long id, LocalDateTime checkoutDateTime) {
    Reservation reservation = reservationRepository.findByIdFetchDiscounts(id)
            .orElseThrow(EntityNotFoundException::new);
    assertUpdatable(reservation);

    // update reservation's final dates (if the guest decides to check out earlier than expected)
    if (checkoutDateTime != null) {
        if (checkoutDateTime.toLocalDate().isAfter(reservation.getToDateTime().toLocalDate()) ||
            checkoutDateTime.isBefore(reservation.getFromDateTime())) {
            throw new IllegalArgumentException(
                    "Can't make the check-out when the check-out date is after the interval of the reservation" +
                            "or the check-out date is before the check-in date, id: " + id
            );
        }
        updateReservationDates(reservation, reservation.getFromDateTime(), checkoutDateTime);
    }
    // create a receipt for the reservation and save it
    double totalPrice = countTotalPrice(reservation);
    reservation.createReceipt(totalPrice);
    return receiptMapper.entityToReceiptResponse(reservation.getReceipt());
}
```
* `ReservationResponseDto remove(Long id)` - видаляє резервацію за id.
* `List<ReservationResponseDto> getAll()` - дістає всі ReservationResponseDto з бд.
* `List<ReservationResponseDto> getAllNonCheckedOut()` - дістає всі ReservationResponseDto з бд, у яких немає чеку.
* `ReservationResponseDto get(Long id)` - дістає резервацію за id з бд.

ReservationDto:
```java
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDto {

    @NotNull
    private LocalDateTime from;

    @NotNull
    private LocalDateTime to;

    @NotNull
    private Long roomNumber;

    @NotNull
    private String renterEmail;
}
```
#### ReceiptService має публічні методи:
* `ReceiptResponse get(Long id)` - знаходження dto чеку в бд за id.
* `ReceiptResponse remove(Long id)` - видалення чеку за id.
#### RenterService має публічні методи:
* `RenterDto create(RenterDto renterDto)` - створення нового клієнта.
* `RenterDto remove(String email)` - видалення орендаря за email'ом.
* `RenterDto update(String email, RenterDto renterDto)` - оновлення даних орендаря.
* `<T> T get(String email, Class<T> type)` - пошук dto або сутності орендаря за email'ом.
* `Renter get(String email)` - пошук сутності орендаря за email'ом.
* `<T> List<T> getAll(Class<T> type)` - пошук всіх dto або сутностей орендаря.

### Controllers
#### AdministratorController (шлях /administrator):
* Створює адміністратора, якщо у аутентифікованого адміністратора є роль ADMIN:
   ```
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<AdministratorDto> create(@Valid @RequestBody AdministratorDto administratorDto)
  ```
* Видаляє адміністратора за email'ом, якщо у аутентифікованого адміністратора є роль ADMIN, або якщо його email дорівнює email'у з аргументу метода:  
  ```
  @DeleteMapping("/{email}")
  @PreAuthorize("authentication.name == #email or hasRole('ADMIN')")
  public ResponseEntity<AdministratorDto> delete(HttpSession session, @PathVariable("email") String email)
  ```
* Оновлює дані адміністратора за email'ом, якщо у аутентифікованого адміністратора є роль ADMIN, або якщо його email дорівнює email'у з аргументу метода:
  ```
  @PutMapping("/{email}")
  @PreAuthorize("authentication.name == #email or hasRole('ADMIN')")
  public ResponseEntity<AdministratorDto> update(@PathVariable("email") String email,
                                                 @Valid @RequestBody UpdateAdministratorDto administratorDto)
  ```
*  Дістає ResponseAdministratorDto за значенням параметру email. Всі адміністратори мають доступ до цього методу:
  ```
  @GetMapping("/{email}")
  public ResponseEntity<ResponseAdministratorDto> get(@PathVariable("email") String email)
  ```
*  Дістає всі ResponseAdministratorDto з бд. Всі адміністратори мають доступ до цього методу:
  ```
  @GetMapping
  public ResponseEntity<List<ResponseAdministratorDto>> getAll()
  ```

#### LoginController (шлях /authenticate):
* Метод дістає екземпляр класу `org.springframework.security.core.userdetails.User` з SecurityContext'у та повертає його:
  ```
  @PostMapping
  public ResponseEntity<User> authenticate()
  ```

#### ReceiptController (шлях /reservation/{id}/receipt):
* Дістає чек за id:
  ```
  @GetMapping
  public ResponseEntity<ReceiptResponse> getReceipt(@PathVariable("id") Long id)
  ```
* Видаляє чек за id:
  ```
  @DeleteMapping
  public ResponseEntity<ReceiptResponse> deleteReceipt(@PathVariable("id") Long id)
  ```

#### RenterController (шлях /renter):
* Створення орендаря:
  ```
  @PostMapping
  public ResponseEntity<RenterDto> create(@Valid @RequestBody RenterDto renterDto)
  ```
* Видалення орендаря за email:
  ```
  @DeleteMapping("/{email}")
  public ResponseEntity<RenterDto> remove(@PathVariable("email") String email)
  ```
* Оновлення орендаря за email:
  ```
  @PutMapping("/{email}")
  public ResponseEntity<RenterDto> update(@PathVariable("email") String email,
                                          @Valid @RequestBody RenterDto renterDto)
  ```
* Пошук орендаря за email:
  ```
  public ResponseEntity<RenterResponseDto> get(@PathVariable("email") String email)
  ```
* Пошук всіх орендарів з бд:
  ```
  public ResponseEntity<List<RenterResponseDto>> getAll()
  ```

#### ReservationController (шлях /reservation):
* Створення резервації:
  ```
  @PostMapping
  public ResponseEntity<ReservationResponseDto> create(@Valid @RequestBody ReservationDto reservationDto)
  ```
* Оновлення резервації за id:
  ```
  @PutMapping("/{id}")
  public ResponseEntity<ReservationResponseDto> update(@PathVariable("id") Long id,
                                                       @Valid @RequestBody ReservationUpdateDto reservationDto)
  ```
* Видалення резервації за id:
  ```
  @DeleteMapping("/{id}")
  public ResponseEntity<ReservationResponseDto> delete(@PathVariable("id") Long id)
  ```
* Пошук резервації за id:
  ```
  @GetMapping("/{id}")
  public ResponseEntity<ReservationResponseDto> get(@PathVariable("id") Long id)
  ```
* Пошук всіх резервацій:
  ```
  @GetMapping
  public ResponseEntity<List<ReservationResponseDto>> getAll()
  ```
* закінчення резервації із випискою чеку за id:
  ```
  @PostMapping("{id}/checkout")
  public ResponseEntity<ReceiptResponse> checkout(@PathVariable("id") Long id,
                                                         @RequestParam(required = false) LocalDateTime checkoutDateTime)
  ```

#### RoomController (шлях /room):
* Створення кімнати:
  ```
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RoomDto> create(@Valid @RequestBody RoomDto roomDto)
  ```
* Оновлення кімнати за номером:
  ```
  @PutMapping("/{roomNumber}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RoomDto> update(@PathVariable("roomNumber") Long roomNumber,
                                        @RequestBody RoomDto roomDto)
  ```
* Видалення кімнати за номером:
  ```
  @DeleteMapping("/{roomNumber}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RoomDto> remove(@PathVariable("roomNumber") Long roomNumber)
  ```
* Пошук кімнати за номером:
  ```
  @GetMapping("/{roomNumber}")
  public ResponseEntity<RoomDto> get(@PathVariable("roomNumber") Long roomNumber)
  ```
* Пошук всіх кімнат:
  ```
  @GetMapping
  public ResponseEntity<List<RoomDto>> getAll()
  ```
* Пошук всіх кімнат, які вільні станом на поточний день:
  ```
  @GetMapping("/free")
  public ResponseEntity<List<RoomDto>> getAllFree()
  ```
* Пошук всіх кімнат, які вільні ні проміжку за параметрів:
  ```
  @GetMapping("/free/dates")
  public ResponseEntity<List<RoomDto>> getAllFree(@RequestParam(value = "from") LocalDate from,
                                                  @RequestParam(value = "to") LocalDate to)
  ```

### Security
Аутентифікація адміністраторів здійснюється за допомогою HTTP Basic Auth. Було імплементовано лише `UserDetailsService`:
```java
@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {

    private final AdministratorRepository administratorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Administrator administrator = administratorRepository.findByEmailFetchRoles(email)
                .orElseThrow(EntityNotFoundException::new);

        String[] roleNames = retrieveRoleNames(administrator.getRoles());
        return User.withUsername(administrator.getEmail())
                .password(administrator.getPassword())
                .roles(roleNames)
                .build();
    }

    private String[] retrieveRoleNames(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toArray(String[]::new);
    }
}
```

Security конфігурація:
```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable) // todo: handle csrf
                .cors(customizer -> customizer.configurationSource(request -> corsConfiguration()))
                .securityContext(customizer -> customizer.requireExplicitSave(false))
                .authorizeHttpRequests(customizer ->
                        customizer
                                .anyRequest().authenticated()
                )
                .httpBasic(customizer -> customizer.authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())
                        ) // without alert popup form
                );

        return http.build();
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setMaxAge(3600L);
        config.setExposedHeaders(List.of("Authorization")); // allows set the list of response headers other than simple headers
        return config;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### Scheduled task
Було створено метод `forceCheckout`, який автоматично запускається раз на годину. Метод здійснює діставання всіх резервацій, які ще не завершилися
(у яких немає чеку), перевіряє, чи закінчився строк резервації. Якщо так, то буде здійснена спроба завершення резервації за допомогою методу 
`ReservationService#checkout`:
```java
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Log4j2
public class ScheduleConfig {

    private final ReservationService reservationService;

    @Scheduled(fixedRate = 3_600_000)
    public void forceCheckout() {
        log.info("Check out all the guests if the reservation is expired");
        List<ReservationResponseDto> reservations = reservationService.getAllNonCheckedOut();
        LocalDateTime now = LocalDateTime.now();

        for (ReservationResponseDto reservation : reservations) {
            if (now.isAfter(reservation.getTo())) {
                try {
                    reservationService.checkout(reservation.getId());
                // the guest with the current reservation may be concurrently checked out
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }
}
```

### Tests
Було створено базу даних для тестувань. Реалізовано модульні тести для репозиторіїв, сервісів, моделей (каскадне видалення, додавання, оновлення, orphan removal).
