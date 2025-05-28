# 🎪 SWU FESTIVAL 백엔드 레포지토리
서울여자대학교 멋쟁이사자처럼 13기 운영진 축제 프로젝트 Back-end 레포지토리
<br>
<br>

## 📸 서비스 미리보기

### 1. 축제 사이트 오픈

![open](https://ifh.cc/g/hphP6t.jpg)

---

### 2. 로그인 방식

QR 인증을 통해 보다 안전하고 재미있게 로그인할 수 있어요!

![login](https://ifh.cc/g/kY0H0k.png)

---

### 3. 별별 취향 - 유형 진단

별자리 퀴즈를 풀며 나의 축제 감상 유형을 알아보세요!

![quiz1](https://ifh.cc/g/t8jPTo.jpg)

---

### 4. 별별 취향 - 쿠폰 이벤트

퀴즈를 맞히면 커피 쿠폰도 받을 수 있어요!

![quiz2](https://ifh.cc/g/5gHOYn.png)

---

### 5. 부스 배치도

학과 부스, 플리마켓, 푸드트럭 등 다양한 부스 정보를 한눈에 확인할 수 있어요!

![booth](https://ifh.cc/g/C6wovW.jpg)

---

### 6. 플리마켓 / 푸드트럭 / 슈니네컷

각 부스별 상세 위치 및 상품 정보도 확인할 수 있어요!

![market](https://ifh.cc/g/5vpbVa.jpg)

---

### 7. 공연 정보

아티스트 및 동아리 공연 정보를 시간대별로 확인할 수 있어요!

![concert](https://ifh.cc/g/CnRQqN.jpg)

---

## 🛠 Tech Stack

- Java 17 / Spring Boot 3.x  
- Spring Security, JPA, MySQL  
- CLOVA OCR, Locust
- GitHub Actions, Cloudtype

---

## 📂 프로젝트 구조

```
  📂 likelion_13th_swu_festival_backend
├── aop/
│   ├── CustomSpringELParser
│   ├── RedissonLock
│   └── RedissonLockAspect                      
├── config/                   
│   ├── AppConfig
│   ├── CorsConfig
│   ├── RedissonConfig
│   ├── SecurityConfig
│   └── SwaggerConfig
├── constant/                 
│   └── QuizStatus
├── controller/               
│   ├── AnswerController
│   ├── BoothController
│   ├── InitController
│   ├── QuizController
│   └── UserController
├── converter/                
│   └── UserConverter
├── dto/                      
│   ├── answerDTO/
│   │   ├── AnswerChoiceDto
│   │   └── AnswerReturnDto
│   ├── boothDTO/
│   │   ├── BoothInfoResponse
│   │   └── BoothStatusResponse
│   ├── quizDTO/
│   │   ├── QuizAnswerResponseDto
│   │   ├── QuizPercentResponse
│   │   └── QuizResponseDto
│   └── userDTO/
│       ├── UserRequestDTO
│       └── UserResponseDTO
├── entity/                  
│   └── common/
│       └── BaseEntity        
│   ├── Answer
│   ├── Booth
│   ├── Quiz
│   ├── User
│   └── UserBooth
├── jwt/                      
│   ├── JwtUtil
│   └── TokenStatus
├── repository/               
│   ├── AnswerRepository
│   ├── BoothRepository
│   ├── QuizRepository
│   ├── UserBoothRepository
│   └── UserRepository
├── security/                 
│   └── CustomUserDetails
├── service/                  
│   ├── AnswerService
│   ├── BoothService
│   ├── InitService
│   ├── QuizAnswerService
│   ├── QuizService
│   ├── UserService
│   └──  QuizDataInitializer
└── Likelion13thSwuFestivalBackendApplication

  ```

---

## 👥 제작

서울여자대학교 멋쟁이사자처럼 13기
Back-end Contributors:

- [@Seoheeuhm](https://github.com/Seoheeuhm) (Seoheeuhm)
- [@westzerosilver](https://github.com/westzerosilver) (westzerosilver)
- [@yeessonng](https://github.com/yeessonng) (yeeun nam)
- [@yujining3827](https://github.com/yujining3827) (yujining3827)


<br>

## 🔗 배포 링크
👉 https://likelion13th-swu.site



