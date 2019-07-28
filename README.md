# Excel-to-HWP-Converter (macro)

### Project Name : intellij_macro


## 사용한 라이브러리
* Apache POI 
* https://github.com/neolord0/hwplib **neolord0**님의 HWP클래스들을 이용하였습니다.

Excel의 특정 Column 값들을 HWP 파일의 특정 테이블 셀들에 옮길 수 있는 프로그램입니다.
이를 위해서는 한글파일의 특정 셀들에 Field Name 속성값이 설정되어있어야 합니다.
Excel의 Sheet, Row, Column 값은 기본적으로 ***0부터 읽어야 합니다.***


현재 엑셀에서의 데이터의 개수와 한글파일에서의 셀 개수는 수동으로 맞춰주셔야 합니다.

> 추후 수정사항
>> 1. number 입력칸에 숫자만 입력 가능해야 하도록 수정예정
>> 2. ProgressBar 표시 예정
