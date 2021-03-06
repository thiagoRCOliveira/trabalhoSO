package Controllers;

import Beans.Processo;
import Utils.EnumEstado;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerProcessoRR extends Thread implements Initializable {
    @FXML public Label id;
    @FXML public Label totalTempo;
    @FXML public Label estado;
    @FXML public Label tempoRestante;
    @FXML public Label quantum;
    public int quantumValor = 0;
    public Processo processo;
    public ControllerProcessoRR controllerProcessoRR;
    public Parent root;
    @FXML public AnchorPane anchorpan;

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        processo = new Processo();

        id.setText("" + processo.getPid());
        totalTempo.setText("" + processo.getTempoExecucaoTotal());
        tempoRestante.setText("" + processo.getTempoRestante());
        estado.setText("" + processo.getEstado());
        quantum.setText("" + processo.getQuantumRestante());



    }
    public void brilhaPurpurina(int red, int green,int blue){

            final Animation animation = new Transition() {

                {
                    setCycleDuration(Duration.millis(1500));
                    setInterpolator(Interpolator.EASE_OUT);
                }

                @Override
                protected void interpolate(double frac) {
                    Color vColor = new Color(red, green, blue, 1 - frac);
                    anchorpan.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            };
            animation.play();



    }


    public int getProcessoTempoTotalProcesso() {
        return processo.getTempoExecucaoTotal();
    }

    public int getControllerTempoTotal() {
        return Integer.parseInt(totalTempo.getText());
    }

    public void start() {

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                while (true) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            if (processo.getEstado() == EnumEstado.PRONTO) {
                                quantumValor = 0;
                                processo.setQuantumRestante(0);
                                brilhaPurpurina(0,1,0);

                            }
                            if (processo.getEstado() == EnumEstado.EXECUTANDO
                                    && processo.getTempoRestante() > 0) {
                                processo.setTempoRestante(processo.getTempoRestante() - 1);

                                processo.setQuantumRestante(processo.getQuantumRestante()+1);
                                brilhaPurpurina(1,0,0);

                            }
                            if (processo.getTempoRestante() == 0) {
                                processo.setEstado(EnumEstado.FINALIZADO);
                                brilhaPurpurina(0,0,1);

                            }
                            if (processo.getTempoLimite() == 0) {
                                processo.setEstado(EnumEstado.ABORTADO);
                            }
                            if (processo.getEstado() == EnumEstado.ESPERANDO) {
                                processo.setQuantumRestante(0);
                                brilhaPurpurina(1,1,0);
                            }

                            id.setText("" + processo.getPid());
                            totalTempo.setText("" + processo.getTempoExecucaoTotal());
                            tempoRestante.setText("" + processo.getTempoRestante());
                            estado.setText("" + processo.getEstado());
                            quantum.setText("" + processo.getQuantumRestante());

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

}
