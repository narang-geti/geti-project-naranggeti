### CNN-MobileNet을 활용한 중고 전자제품 등급 분류 서비스 
멤버: 황인선 이수빈 민형준 여효진 김영규 문세희
## 프로젝트 소개

'[인텔] AI For Future Workforce를 활용한 인공지능 인재 양성과정'의 두번째 팀 프로젝트임.
프로젝트 기간: 2023.11.08 ~ 11.21
팀원: 황인선 이수빈 민형준 여효진 김영규 문세희

‘이미지 처리(Computer Vision)를 통해 아이폰 중고 제품의 등급을 분류(Classification)해주는 AI 솔루션’을 제공하여 아이폰 중고거래 판매자 및 구매자에게 등급 정보를 제공함.
스마트폰 카메라로 휴대폰의 외관을 촬영하여 등급 정보를 부여함으로써 제품 외관 상태에 대한 객관적인 기준을 제공하여 중고 전자제품 거래에 대한 사용자의 신뢰도를 높이고자 함.

이를 위해 keras의 MobileNet 모델을 사용하여 휴대폰 전면 및 후면 외관상태에 대해 S,A,B,F 4개의 등급을 분류함. 이후, keras 파일을 tflite로 변환하여 Firebase에 업로드하고 Android Studio로 연동한 후 모바일 앱으로 구현함.

## 1. 기술스택

## Geti Project (2023.11.08 ~ 11.21)
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/25a09a5b-76d0-4ca9-93e0-1d2312cae734)

### 시스템 아키텍처
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/1d5f02c7-7334-4336-b190-eeac3f0b240c)

## Openvino Project (2023.11.23 ~ 12.05)
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/f4882427-5da1-4622-a5b4-42348d942345)
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/f61f50df-346d-4a04-b82a-0bf399e17761)
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/aa3c7538-0618-499a-9746-8d56ee7c1f99)
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/0b54359b-942e-4838-a236-e4aebef16b13)
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/ddb6099a-70a3-4c33-a7e4-12bd954e1a74)
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/c36a2d28-52a2-4e17-9756-10d8f8e81ea6)
![image](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/70be4b1a-4ac8-4fd0-9a5a-3efed6f8e743)

## 시연 영상
![openvino시연 gif](https://github.com/narang-geti/geti-project-naranggeti/assets/124758100/20efee7e-0260-4dee-a977-4520ac8ccf94)

### App&Web Engineers
|<img src="https://avatars.githubusercontent.com/u/124758100?v=4" width="80">|<img src="https://avatars.githubusercontent.com/u/139526120?v=4"  width="80">|<img src="https://avatars.githubusercontent.com/u/139526041?v=4" width="80">|
|:---:|:---:|:---:|
|[문세희](https://github.com/snowball9820)|[김영규](https://github.com/CaptinJackLeader)|[민형준](https://github.com/xax219)|
  <br>
### AI Engineers
|<img src="https://avatars.githubusercontent.com/u/139525853?v=4" width="80">|<img src="https://avatars.githubusercontent.com/u/139526149?v=4"  width="80">|
|:---:|:---:|
|[여효진](https://github.com/penguinetongtong)|[이수빈](https://github.com/dltnqls3119)|
  <br>


발표자료
https://www.canva.com/design/DAF0rZ8KmlE/G-405p392gEhTHk7WyNWOQ/edit?utm_content=DA[…]m_campaign=designshare&utm_medium=link2&utm_source=sharebutton  

ios App    
https://github.com/snowball9820/somac_project_flutter.git






