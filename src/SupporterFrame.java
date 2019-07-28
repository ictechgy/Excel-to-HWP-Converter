import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

class SupporterFrame extends JFrame {
    private static final String title = "Excel data to HWP by AJH (For Tag)";
    private int width = 500;
    private int height = 350;
    private JTextField excelPath;   //고른 엑셀파일의 경로를 보여줄 필드
    private JButton excelButton;    //엑셀 파일 고르기 버튼
    private JFileChooser excelChooser;  //엑셀 파일 고르기 버튼을 누르면 나오는 선택화면

    private File excelFile;
    private JTextField excelSheet;
    private JTextField excelRow;
    private JTextField excelColumn;

    private JTextField hwpPath;
    private JButton hwpButton;
    private JFileChooser hwpChooser;
    private File hwpFile;
    private JTextField hwpField;

    private Container container;

    private JButton cookingButton;


    SupporterFrame(){
        super(title);
        setSize(width, height);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation(screenSize.width/2 - width/2, screenSize.height/2 - height/2); //화면 가운데에 보이기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container = getContentPane();
        container.setLayout(new GridLayout(3,1));

        createExcelPart();
        createHWPPart();
        createResultPart();

        setResizable(false);
        setVisible(true);
    }

    private void createExcelPart(){
        JPanel panel = new JPanel();     //엑셀 파일 전용 패널
        panel.setLayout(new FlowLayout());
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Excel"));

        excelPath = new JTextField();
        excelPath.setColumns(30);
        excelPath.setEditable(false);
        panel.add(excelPath);

        excelButton = new JButton("Choose Excel");

        excelChooser = new JFileChooser();
        excelChooser.setDialogTitle("Choose Excel");
        excelChooser.setFileFilter(new FileNameExtensionFilter("Excel파일(.xlsx)", "xlsx"));
        //excelChooser.addChoosableFileFilter(new FileNameExtensionFilter("Excel파일", "xlsx"));
        excelChooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));

        excelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excelFileButtonClicked();
            }
        });
        panel.add(excelButton);

        //sheet, rowIndex, columnIndex 지정
        //반 정보와 이름 정보를 동시에 불러오도록 만들 수도 있을 것임. 그러면 동시에 불러온 것을 또 동시에 한글에 입력시키는 것도 필요
        excelSheet = new JTextField();
        excelSheet.setColumns(3);
        excelRow = new JTextField();
        excelRow.setColumns(3);
        excelColumn = new JTextField();
        excelColumn.setColumns(3);

        panel.add(new JLabel("Sheet Number "));
        panel.add(excelSheet);

        panel.add(new JLabel("Row Number "));
        panel.add(excelRow);

        panel.add(new JLabel("Column Number "));
        panel.add(excelColumn);

        JTextField alert = new JTextField("주의! excel 파일의 sheet 번호와 row, column 번호는 0번부터 시작합니다.");
        alert.setEditable(false);
        alert.setForeground(new Color(255,0,0));
        alert.setBorder(new EmptyBorder(0,0,0,0));
        panel.add(alert);

        container.add(panel);
    }
    private void createHWPPart(){
        JPanel panel = new JPanel();  //한글 파일 전용 패널
        panel.setLayout(new FlowLayout());
        panel.setBorder(new TitledBorder(new EtchedBorder(), "HWP"));

        hwpPath = new JTextField();
        hwpPath.setColumns(30);
        hwpPath.setEditable(false);
        panel.add(hwpPath);

        hwpButton = new JButton("Choose HWP");

        hwpChooser = new JFileChooser();
        hwpChooser.setDialogTitle("Choose HWP");
        hwpChooser.setFileFilter(new FileNameExtensionFilter("HWP파일(.hwp)", "hwp"));
        hwpChooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));

        hwpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hwpFileButtonClicked();
            }
        });
        panel.add(hwpButton);

        hwpField = new JTextField();
        hwpField.setColumns(10);

        panel.add(new JLabel("Field Name"));
        panel.add(hwpField);

        container.add(panel);
    }
    private void createResultPart(){
        JPanel panel = new JPanel(new FlowLayout());  //변환버튼 등의 결과 전용 패널

        cookingButton = new JButton("Start Cooking(Excel to HWP)");
        cookingButton.setPreferredSize(new Dimension(width/2, height/6));

        cookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCooking();
            }
        });
        panel.add(cookingButton);

        JTextField info = new JTextField("Developer : 안진홍, Github : https://github.com/ictechgy");
        info.setEditable(false);
        info.setForeground(new Color(0,0,255));
        info.setBorder(new EmptyBorder(0,0,0,0));

        panel.add(info);

        container.add(panel);
    }

    private void excelFileButtonClicked(){
        int result = excelChooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){  //확인 버튼을 누른 경우

            String fileName = excelChooser.getSelectedFile().getName();
            String extension = fileName.substring(fileName.lastIndexOf(".")+1);
            if(extension.equals("xlsx")){       //확장자 체크
                excelFile = excelChooser.getSelectedFile();
                excelPath.setText(excelFile.getAbsolutePath());
            }else{
                JOptionPane.showMessageDialog(this, "해당 파일은 사용 할 수 없습니다. xlsx 파일만 지정 할 수 있습니다.");
            }
        }
    }

    private void hwpFileButtonClicked(){
        int result = hwpChooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){

            String fileName = hwpChooser.getSelectedFile().getName();
            String extension = fileName.substring(fileName.lastIndexOf(".")+1);
            if(extension.equals("hwp")){       //확장자 체크
                hwpFile = hwpChooser.getSelectedFile();
                hwpPath.setText(hwpFile.getAbsolutePath());
            }else{
                JOptionPane.showMessageDialog(this, "해당 파일은 사용 할 수 없습니다. hwp 파일만 지정 할 수 있습니다.");
            }
        }
    }

    private void startCooking(){
        try{

            String path = setSaveLocation();    //저장 위치 먼저 설정
            if(path == null){  //저장경로 설정을 취소한 경우.
                return;
            }

            /*현재 ProgressBar가 제대로 안보이는 문제 존재
            JDialog progressDialog = new JDialog(this, true);
            progressDialog.setTitle("진행 중");
            progressDialog.setLayout(new FlowLayout());
            progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);  //닫기 불가

            Dimension screesSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = 250;
            int height = 100;
            progressDialog.setSize(width, height);
            progressDialog.setLocation(new Point(new Point(screesSize.width/2 - width/2, screesSize.height/2 - height/2)));
            progressDialog.setResizable(false);

            JTextField progressText = new JTextField("Progress");
            progressText.setEditable(false);
            progressText.setBorder(new EmptyBorder(0, 0, 0, 0));
            progressDialog.getContentPane().add(progressText);

            JProgressBar progressBar = new JProgressBar();
            progressBar.setMinimum(0);
            progressBar.setMinimum(100);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);

            progressDialog.getContentPane().add(progressBar);

            progressDialog.setVisible(true);
            progressText.setVisible(true);
            progressBar.setVisible(true);
            */


            String excel = excelFile.getAbsolutePath();
            String hwp = hwpFile.getAbsolutePath();

            //progressBar.setValue(20);

            ExcelReader excelReader = new ExcelReader(excel);
            excelReader.setSheetIndex(Integer.parseInt(excelSheet.getText()));
            excelReader.setRowIndex(Integer.parseInt(excelRow.getText()));
            excelReader.setColumnIndex(Integer.parseInt(excelColumn.getText()));

            //progressBar.setValue(40);

            ArrayList<String> list = excelReader.readFile();

            //progressBar.setValue(50);

            HWPComposer hwpComposer = new HWPComposer(hwp);
            hwpComposer.setFieldName(hwpField.getText());
            hwpComposer.findCells();

            //progressBar.setValue(70);

            hwpComposer.adjustCells(list);

            //progressBar.setValue(90);

            hwpComposer.setAfterFilePath(path);
            hwpComposer.writeHWP();

            //progressBar.setValue(100);
            //progressBar.setVisible(false);
            //progressDialog.setVisible(false);

            JOptionPane.showMessageDialog(this, "성공적으로 변환하였습니다! 결과를 확인하세요!");
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "오류 발생\n오류 내용 : " + e.getMessage());
        }
    }

    private String setSaveLocation(){
        String path = null;
        JFileChooser saveLocation = new JFileChooser();
        saveLocation.setDialogTitle("Define Save Location");
        saveLocation.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));
        saveLocation.setFileFilter(new FileNameExtensionFilter("한글파일(.hwp)", "hwp"));       //보여주기일 뿐 실질적으로 저장시에는 확장자 설정이 안됨
        int result = saveLocation.showSaveDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){
            path = saveLocation.getSelectedFile().getAbsolutePath();
            int confirmPath = path.indexOf(".hwp");
            if(confirmPath == -1){
                path += ".hwp";         //사용자가 .hwp 확장자를 작성하지 않은경우 임의 작성
            }
        }

        return path;
    }

}