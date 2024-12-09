import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileCleanerApp extends Application {

    private TextField folderPathField;
    private TextField extensionField;

    @Override
    public void start(Stage primaryStage) {
        // 폴더 경로 입력 필드
        folderPathField = new TextField();
        folderPathField.setMinWidth(300);
        folderPathField.setEditable(false);
        folderPathField.setPromptText("폴더 경로 선택");

        // 확장자 입력 필드
        extensionField = new TextField();
        extensionField.setMinWidth(300);
        extensionField.setPromptText("확장자 (예: .txt)");

        // 경로 선택 버튼
        Button selectFolderButton = new Button("폴더 선택");
        selectFolderButton.setMinWidth(100);
        selectFolderButton.setOnAction(event -> selectFolder());

        // 실행 버튼
        Button runButton = new Button("휴지통으로 파일 보내기");
        runButton.setMinWidth(100);
        runButton.setOnAction(event -> sendFilesToTrash());

        // 레이아웃 구성
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 15, 20, 15)); // 위 20, 오른쪽 15, 아래 20, 왼쪽 15
        layout.setMinSize(4000, 2000);
        layout.getChildren().addAll(folderPathField, extensionField, selectFolderButton, runButton);

        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setTitle("파일 관리 프로그램");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 폴더 경로 선택
    private void selectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            folderPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    // 파일을 휴지통으로 보내는 로직
    private void sendFilesToTrash() {
        String folderPath = folderPathField.getText();
        String extension = extensionField.getText();

        if (folderPath.isEmpty() || extension.isEmpty()) {
            showAlert("경로와 확장자를 입력해주세요.");
            return;
        }

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            showAlert("유효한 폴더 경로를 입력해주세요.");
            return;
        }

        try {
            // 폴더를 재귀적으로 처리
            processDirectory(Paths.get(folderPath), extension);
            showAlert("작업 완료!");
        } catch (IOException e) {
            showAlert("파일 처리 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 지정한 폴더와 하위 폴더를 순차적으로 처리
    private void processDirectory(Path folderPath, String targetExtension) throws IOException {
        // 현재 폴더 내 파일 및 하위 폴더 가져오기
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
            List<File> filesToDelete = new ArrayList<>();
            boolean hasTargetExtension = false;

            for (Path entry : stream) {
                File file = entry.toFile();

                if (file.isDirectory()) {
                    // 하위 디렉터리 재귀 처리
                    processDirectory(entry, targetExtension);
                } else {
                    // 파일 확장자 확인
                    if (file.getName().endsWith(targetExtension)) {
                        hasTargetExtension = true;
                    } else {
                        filesToDelete.add(file);
                    }
                }
            }

            // 지정한 확장자가 있는 경우만 다른 확장자 파일 삭제
            if (hasTargetExtension) {
                for (File file : filesToDelete) {
                    moveToTrash(file);
                }
            }
        } catch (IOException e) {
            System.err.println("디렉터리 처리 중 오류 발생: " + folderPath);
            e.printStackTrace();
        }
    }

    // 휴지통으로 파일을 이동시키는 기능
    private void moveToTrash(File file) {
        try {
            Desktop.getDesktop().moveToTrash(file);  // 휴지통으로 이동
            System.out.println("휴지통으로 이동: " + file.getAbsolutePath());
        } catch (UnsupportedOperationException e) {
            // 휴지통으로 이동이 지원되지 않는 경우 파일 삭제
            System.err.println("휴지통으로 이동 실패: " + file.getAbsolutePath());
//            try {
//                Files.delete(file.toPath());
//                System.out.println("삭제됨: " + file.getAbsolutePath());
//            } catch (IOException ex) {
//                System.err.println("파일 삭제 실패: " + file.getAbsolutePath());
//                ex.printStackTrace();
//            }
        }
    }

    // 알림 창을 띄우는 메서드
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("알림");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
