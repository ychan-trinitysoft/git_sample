import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainController {

    /**
     * 새로운 위젯을 만들고 창을 생성
     * */
    public static void createStage(Application application, Stage primaryStage) {
        try {
            // FXML 파일을 로드
            FXMLLoader loader = new FXMLLoader(application.getClass().getResource("/sample.fxml"));
            // FXML 파일에서 정의한 UI를 Scene으로 만들기
            GridPane root = loader.load();

            Button myButton = (Button) root.lookup("#myButton");
            myButton.setOnAction(event -> {
                createStage2(application, primaryStage);
            });

            // Scene을 생성하고 Stage에 설정
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("FXML Example");// 창 제목 설정
            primaryStage.show();// 창을 생성
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 기존에 있는 창에서 위젯을 변경
     * */
    public static void createStage2(Application application, Stage primaryStage) {
        try {
            // FXML 파일을 로드
            FXMLLoader loader = new FXMLLoader(application.getClass().getResource("/sample2.fxml"));
            // FXML 파일에서 정의한 UI를 Scene으로 만들기
            GridPane root = loader.load();

            Button myButton = (Button) root.lookup("#myButton");
            TextField myTextField = (TextField) root.lookup("#myTextField");
            Label myLabel = (Label) root.lookup("#myLabel");
            myButton.setOnAction(event -> {
                String inputText = myTextField.getText();

                myLabel.setText(inputText);
            });

            // Scene을 생성하고 Stage에 설정
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("FXML Example 2");// 창 제목 설정
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
