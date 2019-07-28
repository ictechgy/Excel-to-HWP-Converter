import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.ParaText;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.objectfinder.CellFinder;
import kr.dogfoot.hwplib.writer.HWPWriter;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

public class HWPComposer {

    private String path;
    private HWPFile hwpFile;
    private String fieldName;
    private String afterFilePath;
    private ArrayList<Cell> cells;
    private int ERR_CODE;  //정상종료는 0
    private boolean isModified;

    public HWPComposer(String path) throws Exception{
        this.path = path;
        hwpFile = HWPReader.fromFile(path);
        ERR_CODE = 0;
        isModified = false;
    }

    public void setAfterFilePath(String afterFilePath) {
        this.afterFilePath = afterFilePath;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void findCells() throws Exception{
        if(hwpFile == null || fieldName == null){
            throw new Exception("파일 또는 필드명 (재)설정이 필요합니다.");
        }
        cells = CellFinder.findAll(hwpFile, fieldName);
        if(cells == null){
            throw new Exception("해당하는 셀을 찾을 수 없습니다.");
        }
    }
    public void adjustCells(@NotNull ArrayList<String> excelList) throws UnsupportedEncodingException {
        if(excelList.size() > cells.size()){ //한글 파일에 존재하는 삽입가능한 공간보다 엑셀 데이터의 개수가 많은 경우
            ERR_CODE = 101;
        }

        Iterator<String> it = excelList.iterator();

        for(Cell c : cells) {   //하나의 셀공간에 접근 - forEach()가능
            Paragraph firstPara = c.getParagraphList().getParagraph(0); //해당 셀 공간에서 첫번째 문단에만 접근

            if(firstPara.getText() != null){  //해당 문단에 값이 존재하는 경우 해당 문단의 값을 삭제. 문단 자체의 틀이 사라지지는 않는다.
                firstPara.deleteText();
                //셀 안의 기존 값이 있는 경우 모두 삭제처리. 다만 엔터값만 있는경우 이를 제대로 삭제하지 못함. 다른 문단으로 인식해서인 듯 - 문단자체 삭제 기능이 존재 X
            }
            firstPara.createText();
            ParaText paraText = firstPara.getText();

            if(it.hasNext()){
                paraText.addString(it.next());
            }else{
                ERR_CODE = 102;  //엑셀 파일의 데이터보다 데이터삽입가능한 한글파일의 셀이 더 많은 경우
                break;
            }
        }
        isModified = true;
    }
    public void writeHWP() throws Exception{
        if(isModified){
            HWPWriter.toFile(hwpFile, afterFilePath);  //파일이 변경된 경우에만 새로 저장될 수 있도록 설정
        }else{
            //파일이 변경되지 않은 경우
        }
    }

}
