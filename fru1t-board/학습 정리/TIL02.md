# Authorization, Authorization
## Session에서 어려운 부분 
- Session Fixation Attack
- Session Hijacking 
- CSRF 
등의 위험이 존재

## jwt에서 어려운 부분 
- jwt는 stateless한 성격이 있으므로 무효화가 어려움

## Hybrid 방식의 이점 
- jwt + Refresh Token의 조합
- 무효화 가능(Revocation)
- Session 쿠키를 사용하면 CSRF 방어가 됨

## Cookie 설정 
- httpOnly, sameSite, secure 설정을 해야함

## Spring Security 
- Spring Security를 적용하면 Filter가 강제로 걸려서 Configuration 으로 설정을 수정해야 할 필요가 있음
- Spring Security의 이점을 잘 모르겠어서 일단 의존성을 제거하고 OnePerRequest를 상속받아서 Filter를 직접 구현하는 방안으로 채택
- Filter 로직에서 GET Method와 그 외 다른 요구사항에 맞게 적절히 허용, 비허용을 구분 

### HTTPS 문제
- 일반적으로 Nginx 같은 웹서버가 앞단에 있으면 WAS는 HTTP로 요청을 받게 됨
- 물론 HTTPS로 요청을 전달할 수 있지만 적절한지 잘 모르겠음
- 또한 WAS에서 현재 Token을 발급할 때 Cookie에 발급을 하는데 Cookie는 HttpOnly, Secure 설정이 적용되어있는 상태

### 적당한 해결과 타협점 
- `proxy_set_header X-Forwarded-Proto https;` 
- Nginx에서 다음과 같이 설정하면 https 설정이 유지가 된다.
- WAS에서는 