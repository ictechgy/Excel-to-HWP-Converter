import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelReader{
    private FileInputStream file;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int sheetIndex;
    private int rowIndex;
    private int columnIndex;  //칼럼Index

    private int ERR_CNT;   //셀값 처리 중 오류(예외)가 발생한 카운트 셈용

    public ExcelReader(String path) throws IOException {
        file = new FileInputStream(path);
        workbook = new XSSFWorkbook(file);
        sheetIndex = -1;
        rowIndex = -1;
        columnIndex = -1;  //인덱스 값들이 정해지지 않은 상태는 -1로 한다.
        ERR_CNT = 0;
    }

    public void setSheetIndex(int sheetIndex){
        this.sheetIndex = sheetIndex;
    }
    public void setRowIndex(int rowIndex){
        this.rowIndex = rowIndex;
    }
    public void setColumnIndex(int columnIndex){
        this.columnIndex = columnIndex;
    }

    public int getErrCnt() {
        return ERR_CNT;
    }
    //반 정보 칼럼과 이름정보 칼럼을 동시에 읽도록 하기보다는 우선 칼럼 하나에 대해서만 읽도록 만들고 사용자의 요구에 따라 나중에 묶어주는 것이 나아보임
    //또는 HashMap 이용. 키값은 rowIndex로 두는 방식으로

    //빈 셀의 값이 있는 경우 "" 로 처리하도록 해야함. 그래야 나중에 묶었을 시 어긋나지 않음

    public ArrayList<String> readFile() throws Exception{
        ERR_CNT = 0; //ERR_CNT 재초기화
        ArrayList<String> list = new ArrayList<>();
        int numOfSheet = workbook.getNumberOfSheets();

        if(sheetIndex == -1 || sheetIndex > numOfSheet){
            throw new Exception("sheet 인덱스 값이 잘못 됨");
        }else if(rowIndex == -1){
            throw new Exception("row 인덱스 값이 잘못 됨");
        }else if(columnIndex == -1){
            throw new Exception("column 인덱스 값이 잘못 됨");
        }else{
            sheet = workbook.getSheetAt(sheetIndex);
            int rows = sheet.getPhysicalNumberOfRows();     //getPhysicalNumberOfRows()는 값이 입력된 줄의 수를 리턴
            //iterator()도 가능하며 getLastRowNum()을 이용 할 수도 있다.

            for(; rowIndex < rows + rowIndex; rowIndex++){
                XSSFRow row = sheet.getRow(rowIndex);

                if(row != null){
                    XSSFCell cell = row.getCell(columnIndex);
                    if(cell == null || cell.getCellType() != CellType.STRING){
                        list.add("");  //특정 셀이 비어있거나 String값이 아닌 경우 공백값으로 처리
                        ERR_CNT++; //에러 카운트 증가
                    }else{
                        list.add(cell.getStringCellValue());
                    }
                }else{
                    continue; //해당 row가 비어있는 경우? null인경우? 다음 행으로 진행(비워놓아도 관계 없음)
                }
            }
        }
        return list;
    }
}
