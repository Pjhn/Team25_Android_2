# Team25_Android_2

![메디투게더_매니저_그래픽](https://github.com/user-attachments/assets/aefa5ed6-c099-4247-b6b4-994026dd8d12)

> 이 저장소는 메디투게더 매니저 전용 서비스를 제공합니다.

</br>

## 🔗 배포 링크

| 문서 | 
|:--------:|
| [안드로이드 앱]() |
| [백엔드]() |

</br>

## 🏥 서비스 기획 의도

<table>
  <tr>
    <td><b>Problem</b></td>
    <td>병원에 갈 때 혼자서 이동하기 힘들거나 의료 절차를 이해하는데 어려움이 있는 환자가 많음</td>
  </tr>
  <tr>
    <td><b>Persona</b></td>
    <td>노인 환자, 거동이 불편한 환자, 보호자 동반 검사가 필요한 환자</td>
  </tr>
  <tr>
    <td><b>사용하는 이유</b></td>
    <td>혼자서 병원을 방문할 수 없거나, 병원 내 복잡한 절차를 혼자 해결하기 어려움<br>
        보호자가 없는 환자가 안전하게 의료 서비스를 이용할 수 있는 서비스가 필요</td>
  </tr>
  <tr>
    <td><b>Aha 모먼트</b></td>
    <td style="color:red"><b>혼자서 병원 이용이 어려웠는데, 이 서비스를 통해 진료부터 귀가까지 모든 과정을 도움 받을 수 있겠구나!</b></td>
  </tr>
</table>

## ✨ 주요 기능
<table>
  <tr>
    <th>기능</th>
    <th>설명</th>
  </tr>
  <tr>
    <td><b>로그인/회원가입</b></td>
    <td>카카오 OAuth 로그인 기능을 제공하며, 카카오 로그인 후 발급된 access token과 refresh token을 통해 JWT 관리를 수행합니다.<br> 
        refresh token의 만료 기간이 7일 이상 남아 있을 경우 자동 로그인도 지원합니다.</td>
  </tr>
  <tr>
    <td><b>매니저 등록</b></td>
    <td>개인 정보와 프로필 이미지, 자격증명서 이미지를 업로드해 매니저 등록 신청이 가능합니다. 신청이 승인되면 메인 화면에서 서비스를 이용할 수 있습니다.</td>
  </tr>
  <tr>
    <td><b>프로필 관리</b></td>
    <td>프로필 페이지에서 프로필(프로필 이미지, 근무 지역, 근무 시간, 자기소개)을 조회 및 수정할 수 있습니다. </br>
    프로필 이미지는 Amazon S3 서비스를 통해 업로드 합니다.</td>
  </tr>
  <tr>
    <td><b>예약 관리</b></td>
    <td>신청 받은 예약에 대한 관리 기능입니다.<br>
        예약 신청, 거절과 동행 상태 변경이 가능합니다.</td>
  </tr>
  <tr>
    <td><b>실시간 동행 정보 전송</b></td>
    <td>현재 동행중인 이용자에게 실시간 동행 상태 정보를 전송할 수 있습니다. 이 기능을 통해 이용자의 보호자가 이용자 앱을 통해 실시간 정보를 확인할 수 있습니다. </td>
  </tr>
</table>

</br>

## ERD

![image](https://github.com/user-attachments/assets/3e494ae1-4385-4df8-b7f3-7ec5ba2e4e89)

## API 명세서
![image](https://github.com/user-attachments/assets/dd0d407e-b23b-4311-830b-2f6f1df9cfd5)
![image](https://github.com/user-attachments/assets/64739142-1154-4576-a57e-3595c429d8f3)
![image](https://github.com/user-attachments/assets/e116606d-f276-4389-ac36-bb7a6b8ebe5a)

## 코드 유지 관리자
|      | **노신**                 | **박정훈**                  | **이창욱**                    |
|:----:|:--------------------------:|:---------------------------:|:-----------------------------:|
|E-Mail| normengdie@pusan.ac.kr     | hoondb@naver.com          | ckddnr5527@gmail.com                |
|GitHub| [Normenghub](https://github.com/Normenghub) | [Pjhn](https://github.com/Pjhn) | [ichanguk](https://github.com/ichanguk) |
|      | <img src="https://github.com/Normenghub.png" width=100px> | <img src="https://github.com/Pjhn.png" width=100px> | <img src="https://github.com/ichanguk.png" width=100px> |

</br>

## 추후 개발 예정 사항
- 카카오톡 알림 기능
