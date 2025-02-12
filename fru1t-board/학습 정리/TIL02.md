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