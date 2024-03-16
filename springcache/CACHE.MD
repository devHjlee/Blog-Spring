# 목적
운영중이던 프로젝트에서 사용자별 권한,즐겨찾기,메뉴별 다국어 정보를 메뉴 이동마다 DB를 통해 가져오고 있었고
해당 정보들은 거의 변동이 없었기에 반복적으로 사용되는 데이터를 redis 메모리에 저장함으로써 애플리케이션의 성능을 향상

## Cache . . .?
데이터나 값을 미리 저장해 두는 임시 저장소를 말합니다. 주로 프로그램이나 시스템의 성능을 향상시키기 위해 사용됩니다.   
캐시는 일반적으로 데이터나 계산 결과를 저장하고, 해당 데이터나 결과가 필요할 때 바로 사용할 수 있도록 합니다.
* 특징
  * 속도 향상: 캐시된 데이터는 메모리나 빠른 저장 장치에 저장되어 있기 때문에 데이터에 빠르게 접근할 수 있습니다. 이로 인해 데이터 액세스 시간이 단축되어 전반적으로 프로그램이 더 빨리 실행됩니다.
  * 자원 절약: 캐시를 사용하면 매번 데이터를 새로 계산하거나 외부에서 데이터를 가져오는 대신에 이전에 계산된 결과를 재사용할 수 있습니다. 이렇게 함으로써 시스템의 자원 사용이 최적화됩니다.
  * 부하 감소: 데이터나 계산 결과를 미리 캐시해 두면 외부 서비스나 데이터베이스에 대한 요청 횟수를 줄일 수 있습니다. 이는 서버의 부하를 감소시키고 더 많은 요청을 처리할 수 있게 합니다.
  * 데이터 일관성 유지: 캐시된 데이터는 주로 변하지 않는 데이터를 저장하므로, 데이터 일관성을 유지하기 쉽습니다. 필요할 때마다 캐시를 업데이트하거나 재설정하여 일관성을 유지할 수 있습니다.

* 주의점
  * 데이터 무결성(cache-miss): 캐시된 데이터가 항상 최신이고 정확한지 확인해야 합니다. 만약 캐시된 데이터가 오래된 경우에는 사용자에게 잘못된 정보를 제공할 수 있습니다.
  * 캐시 크기 관리: 캐시가 너무 커지면 메모리나 디스크 공간을 많이 차지할 수 있습니다. 이로 인해 시스템 성능이 저하되거나 다른 애플리케이션에 영향을 줄 수 있습니다. 따라서 캐시 크기를 제한하고 정기적으로 캐시를 정리하는 메커니즘을 구현하는 것이 중요합니다.
  * 캐시 무효화: 데이터가 업데이트되면 캐시된 데이터도 함께 업데이트해야 합니다. 데이터가 무효화되었을 때 캐시를 갱신하거나 삭제하는 방법을 고려해야 합니다.
  * 캐시 동시성: 여러 사용자가 동시에 캐시를 업데이트하려고 할 때 발생할 수 있는 동시성 문제를 고려해야 합니다. 이를 위해 적절한 동기화 메커니즘을 구현하거나 캐시 서버를 사용하는 등의 방법을 고려할 수 있습니다.
  * 캐시 키 관리: 캐시 키를 고유하게 유지하고 관리해야 합니다. 중복된 키나 충돌이 발생하면 잘못된 데이터가 반환될 수 있습니다.
  * 캐시 실패 처리: 캐시에서 데이터를 가져오는 작업이 실패할 수 있습니다. 이러한 경우에 대비하여 실패 처리 메커니즘을 구현하여 적절한 에러 핸들링을 수행해야 합니다.
  * 적절한 시기에 사용: 모든 데이터나 연산을 캐시로 저장하는 것은 효율적이지 않을 수 있습니다. 따라서 어떤 데이터나 연산을 캐시에 저장할지를 신중하게 결정해야 합니다.


## 예제소스
### https://github.com/devHjlee/devHjBlog/tree/main/springJpaBulk

## 개발환경
* spring boot : 3.2.3
* OpenJdk 17
* Mysql 8.0
* redis 3.0.504
* spring-boot-starter-data-jpa
* spring-boot-starter-data-redis
* spring-boot-starter-cache
* mysql-connector-j


## 프로젝트 구조
![img0.png](img0.png)

### 예제 소스
#### RedisConfig
* Redis 연결 설정
``` java   
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
```

#### SpringcacheApplication
* @EnableCaching 추가
``` java
@SpringBootApplication
@EnableCaching
public class SpringcacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcacheApplication.class, args);
    }

}
```

#### FavoriteMenuService
* @Cacheable
  * 메서드의 결과를 캐시에 저장하고, 같은 입력 매개변수가 주어졌을 때 캐시에서 결과를 반환합니다. 메서드가 호출될 때마다 캐시에 저장된 결과를 확인하고, 이미 캐시에 있는 경우에는 해당 결과를 반환하고, 없는 경우에는 메서드를 실행하여 결과를 캐시에 저장하고 반환합니다. 이러한 방식으로 이후 동일한 입력에 대한 호출은 캐시된 결과를 반환하여 메서드 실행을 생략할 수 있습니다.
* @CacheEvict
  * 지정된 캐시를 비워서 캐시된 데이터를 갱신하거나 삭제하는 데 사용됩니다. 메서드 실행 후에 지정된 캐시를 비웁니다. 이것은 데이터 변경이 발생했을 때 기존 캐시를 갱신하기 위해 사용됩니다.
``` java   
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteMenuService {
    private final FavoriteRepository menuRepository;

    @Transactional
    @CacheEvict(value = "FavoriteMenu", key = "#userId")
    public void saveMenu(String userId, String favoriteMenu) {
        menuRepository.save(FavoriteMenu.builder().userId(userId).menuName(favoriteMenu).build());
    }

    @Transactional
    @Cacheable(value = "FavoriteMenu", key = "#userId")
    public List<FavoriteMenu> findByUserId(String userId) {
        return menuRepository.findByUserId(userId);
    }
}
```

#### Test Code
```java   
@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteMenuService favoriteMenuService;

    @GetMapping("/favorite")
    public List<FavoriteMenu> getFavoriteMenu() {
        return favoriteMenuService.findByUserId("user123");
    }

    @GetMapping("/saveFavorite") //저장시 CacheEvict을 통해 redis 데이터 삭제
    public void saveFavorite() {
        favoriteMenuService.saveMenu("user123","test");
    }
}

@SpringBootTest
class FavoriteMenuServiceTest {
  @Autowired
  private FavoriteMenuService favoriteMenuService;

  @Test
  void test() {
    String userId = "user123";
    for(int i = 0; i < 1000; i++) {
      favoriteMenuService.saveMenu(FavoriteMenu.builder().userId(userId).menuName("MenuTest"+i).build());
    }
    //같은 조회 메소드 호출
    List<FavoriteMenu> dbList = favoriteMenuService.findByUserId(userId); // DB 조회
    List<FavoriteMenu> cacheList = favoriteMenuService.findByUserId(userId); // Redis 조회

    Assertions.assertEquals(dbList.size(),cacheList.size());

  }
}
```   

### DB 조회와 Redis 에 대한 단순 성능 비교
* DB 조회
  ![img1.png](img1.png)
    * 17초...
* Redis 조회
  ![img2.png](img2.png)
    * 16초...
* identityJDBCBatch()
  ![img3.png](img3.png)
    * 0.5초



