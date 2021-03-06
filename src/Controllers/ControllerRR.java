package Controllers;

import App.MainApp;
import Utils.EnumEstado;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ControllerRR extends Thread implements Initializable {
    @FXML
    public GridPane gridpane;
    @FXML
    public HBox hBoxCore = new HBox();
    @FXML
    public HBox hBoxProcesso = new HBox();
    @FXML
    public HBox hBoxDeadLine = new HBox();
    @FXML
    public TextField textfield_processos;
    @FXML
    public TextField textfieldCores;
    @FXML
    public TextField textField_quantum;


    ArrayList<ControllerProcessoRR> processoArrayList = new ArrayList<>();
    ArrayList<ControllerProcessoRR> coreArrayList = new ArrayList<>();
    ArrayList<ControllerProcessoRR> finalizados = new ArrayList<>();
    private MainApp mainApp;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void start() {

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                while (true) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            arrochaNoProcessador();
                        }
                    });
                    Thread.sleep(1000);
                }
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();


    }

    public void arrochaNoProcessador() {

        if (coreArrayList.size()< Integer.parseInt(textfieldCores.getText())&& processoArrayList.size()>0){
            moveParaProcessador();
        }

        for (int i = 0; i <coreArrayList.size() ; i++) {

            if (coreArrayList.get(i).processo.getEstado()==EnumEstado.FINALIZADO){
                moveParaFinalizados(i);
                break;
            }

            if (coreArrayList.get(i).processo.getQuantumRestante()>= Integer.parseInt(textField_quantum.getText())){
                moveParaAptos(i);
                break;
            }
        }
//       if (coreArrayList.size() < Integer.parseInt(textfieldCores.getText()) && processoArrayList.size() > 0) {
//            coreArrayList.add(processoArrayList.get(0));
//            coreArrayList.get(coreArrayList.size() - 1).start();
//            coreArrayList.get(coreArrayList.size() - 1).processo.setEstado(EnumEstado.EXECUTANDO);
//            processoArrayList.remove(0);
//            hBoxCore.getChildren().add(hBoxProcesso.getChildren().get(0));
//            hBoxProcesso.getChildren().clear();
//            for (int i = 0; i < processoArrayList.size(); i++) {
//
//                hBoxProcesso.getChildren().add(processoArrayList.get(i).getRoot());
//            }
//            System.out.println("entrou na insercao 1");
//
//        }
//
//        //checa se o tamanho do processador lita < valor dos cores
//
//        for (int i = 0; i < coreArrayList.size(); i++) {
//            if (coreArrayList.get(i).quantumValor == Integer.parseInt(textField_quantum.getText())) {
//                processoArrayList.add(coreArrayList.get(i));
//                coreArrayList.get(i).processo.setEstado(EnumEstado.ESPERANDO);
//
//                hBoxCore.getChildren().remove(i);
//                hBoxProcesso.getChildren().add(processoArrayList.get(processoArrayList.size() - 1).getRoot());
//
//
//            }
//            if (coreArrayList.get(i).processo.getEstado() == EnumEstado.FINALIZADO) {
//
//                System.out.println("entrou no insrcao e rmocao");
//
//                finalizados.add(coreArrayList.get(i));
//                coreArrayList.remove(i);
//
//                hBoxCore.getChildren().remove(i);
//
//                hBoxCore.getChildren().add(i, processoArrayList.get(0).getRoot());
//                coreArrayList.add(i, processoArrayList.get(0));
//
//                processoArrayList.get(0).start();
//                processoArrayList.get(0).processo.setEstado(EnumEstado.EXECUTANDO);
//
//                processoArrayList.remove(0);
//
//                hBoxProcesso.getChildren().remove(0);
//
//                hBoxDeadLine.getChildren().add(finalizados.get(finalizados.size() - 1).getRoot());
//
//
//            }
//        }
//

    }

    public void moveParaAptos(int i) {

        processoArrayList.add(coreArrayList.get(i));
        coreArrayList.get(i).processo.setEstado(EnumEstado.ESPERANDO);
        hBoxCore.getChildren().remove(coreArrayList.get(i).getRoot());
        hBoxProcesso.getChildren().add(coreArrayList.get(i).getRoot());

        coreArrayList.remove(i);


    }

    public void moveParaFinalizados(int i) {
        finalizados.add(coreArrayList.get(i));


        hBoxCore.getChildren().remove(coreArrayList.get(i).getRoot());
        coreArrayList.remove(i);

        hBoxDeadLine.getChildren().add(finalizados.get(finalizados.size()-1).getRoot());



    }

    public void moveParaProcessador() {

        coreArrayList.add(processoArrayList.get(0));
        coreArrayList.get(coreArrayList.size() - 1);
        coreArrayList.get(coreArrayList.size() - 1).processo.setEstado(EnumEstado.EXECUTANDO);
        processoArrayList.remove(0);
        hBoxCore.getChildren().add(coreArrayList.get(coreArrayList.size()-1).getRoot());

    }


    public void inserirProcessos(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/processo.fxml"));

        Parent root = (Parent) loader.load();
        processoArrayList.add(loader.getController());
        processoArrayList.get(processoArrayList.size() - 1).setRoot(root);
        hBoxProcesso.getChildren().add(processoArrayList.get(processoArrayList.size() - 1).getRoot());
        hBoxProcesso.setSpacing(10);

    }


    public void inserirNucleos(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Core.fxml"));

        Parent root = (Parent) loader.load();
        coreArrayList.add(loader.getController());

        hBoxCore.getChildren().add(root);
        hBoxCore.setSpacing(10);
        hBoxProcesso.getChildren().remove(0);

    }


    public void start(ActionEvent actionEvent) {
        processoArrayList.get(0).processo.setEstado(EnumEstado.EXECUTANDO);


    }


    public void btn_SJFGO(ActionEvent actionEvent) throws IOException {

        for (int i = 0; i < Integer.parseInt(textfield_processos.getText()); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/processoRR.fxml"));

            Parent root = (Parent) loader.load();
            processoArrayList.add(loader.getController());

            processoArrayList.get(processoArrayList.size() - 1).setRoot(root);


        }
        //Ordena
//        processoArrayList.sort(Comparator.comparingInt(ControllerProcessoRR::getProcessoTempoTotalProcesso));


        for (int i = 0; i < processoArrayList.size(); i++) {
            hBoxProcesso.getChildren().add(processoArrayList.get(i).getRoot());
            processoArrayList.get(i).start();
        }
        start();

    }

    public void processoMais1(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/processoRR.fxml"));

        Parent root = (Parent) loader.load();
        processoArrayList.add(loader.getController());
        processoArrayList.get(processoArrayList.size() - 1).setRoot(root);
        processoArrayList.get(processoArrayList.size()-1).start();
        hBoxProcesso.getChildren().add(processoArrayList.get(processoArrayList.size()-1).getRoot());



    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
