package controllers;
import java.io.File;
import java.sql.*;

import persistencia.importarSQL;
import clases.Clerigo;
import clases.Parroquia;
import clases.Vicaria;
import clases.VistaReporte;
import utilities.interfaces.cargarClerigos;
import utilities.interfaces.cargarVicarias;
import utilities.interfaces.cargarVistaReporte;
import utilities.interfaces.guardarParroquiaSQL;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import persistencia.*;
import utilities.RegexPatterns;
import javafx.scene.control.ChoiceBox;

import java.time.LocalDate;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;

import static utilities.guardarArchivoEnRuta.guardarArchivo;
// Clase de los controladores java fx y los metodos que implementan
public class ArquidiocesisController implements cargarClerigos, guardarParroquiaSQL, cargarVicarias {
    @FXML
    private AnchorPane ancorPane1;

    @FXML
    private Button botonDescargar;

    @FXML
    private Button botonExaminar;

    @FXML
    private Button botonIniciar;

    @FXML
    private Button botonMenuProceso;

    @FXML
    private Button botonMenuReporte;

    @FXML
    private Button botonMenuSubirArchivo;

    @FXML
    private Button botonRegresoInicio;

    @FXML
    private Button botonSubir;


    @FXML
    private ImageView gifCatedral;


    @FXML
    private ImageView imageCatedral;

    @FXML
    private Label labelArquidiocesis1;

    @FXML
    private Label labelArquidiocesis2;

    @FXML
    private Label labelArquidiocesis3;

    @FXML
    private Label labelDinamico;

    @FXML
    private Label labelSeleccione;

    @FXML
    private VBox menuBotones;


    @FXML
    private Rectangle rectanguloFondo;

    @FXML
    private Rectangle rectanguloFondo1;

    @FXML
    private Rectangle rectanguloInicio;

    @FXML
    private Rectangle rectanguloMenu;

    @FXML
    private Button registroBotonEnviar;

    @FXML
    private DatePicker registroDatePickerFF;

    @FXML
    private Label registroLabelCiudadPertenece;

    @FXML
    private Label registroLabelDescripcion;

    @FXML
    private Label registroLabelDireccionParroquia;

    @FXML
    private Label registroLabelEmail;

    @FXML
    private Label registroLabelFechaF;

    @FXML
    private Label registroLabelNombreParroquia;

    @FXML
    private Label registroLabelParroco;

    @FXML
    private Label registroLabelSitioWeb;

    @FXML
    private Label registroLabelTelefono;

    @FXML
    private Label registroLabelVicaria;

    @FXML
    private ChoiceBox<String> registroChoiceBoxCiudad;

    @FXML
    private ComboBox<Clerigo> registroComboBoxParroco;

    @FXML
    private ChoiceBox<Vicaria> registroChoiceBoxVicaria;

    @FXML
    private TextField registroTxtFieldEmail;

    @FXML
    private TextField registroTxtFieldSitioWeb;

    @FXML
    private TextField registroTxtFieldTelefono;

    @FXML
    private TextField registrotextFieldDireccion;

    @FXML
    private TableView<VistaReporte> tablaReporte;

    @FXML
    private TextField textFieldRutaArchivo;

    @FXML
    private TextField txtFieldNombreParroquia;

    @FXML
    private TableColumn<VistaReporte, Integer> planificados;

    @FXML
    private TableColumn<VistaReporte, String> presupuestoTotal;

    @FXML
    private TableColumn<VistaReporte, Integer> proyectosTotales;
    @FXML
    private TableColumn<VistaReporte, String> vicariaResponsable;
    @FXML
    private TableColumn<VistaReporte, String> inversionPromedio;
    @FXML
    private TableColumn<VistaReporte, Integer> canceladosSuspendidos;

    @FXML
    private TableColumn<VistaReporte, Integer> enEjecucion;


    private File archivoSeleccionado;

//metodo que guarda los párrocos en el comboBox
    void setRegistroComboBoxParroco(){
        registroComboBoxParroco.getItems().clear();
        registroComboBoxParroco.getItems().addAll(cargarClerigos.cargar());
    }


    @FXML
    void mostrarMenu(ActionEvent event) {
        labelArquidiocesis2.setVisible(true);
        labelArquidiocesis1.setVisible(false);
        gifCatedral.setVisible(false);
        rectanguloFondo.setVisible(false);
        rectanguloFondo1.setVisible(true);
        rectanguloInicio.setVisible(false);
        rectanguloMenu.setVisible(true);
        menuBotones.setVisible(true);
        botonIniciar.setVisible(false);
        labelSeleccione.setVisible(true);
        imageCatedral.setVisible(true);

    }
    //metodo para regresar al menu principal
    @FXML
    void regresarInicio(ActionEvent event) {
        initialize();
    }

    @FXML
    void initialize(){
        registroChoiceBoxVicaria.getItems().clear();
        registroDatePickerFF.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Deshabilitar fechas futuras
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
        setRegistroComboBoxParroco();
        labelArquidiocesis2.setVisible(false);
        labelArquidiocesis1.setVisible(true);
        gifCatedral.setVisible(true);
        rectanguloFondo.setVisible(true);
        rectanguloFondo1.setVisible(false);
        rectanguloInicio.setVisible(true);
        rectanguloMenu.setVisible(false);
        menuBotones.setVisible(false);
        labelSeleccione.setVisible(false);
        botonIniciar.setVisible(true);
        labelArquidiocesis2.setLayoutY(40);
        labelArquidiocesis2.setLayoutX(245);
        labelDinamico.setVisible(false);
        textFieldRutaArchivo.setVisible(false);
        botonExaminar.setVisible(false);
        botonSubir.setVisible(false);
        textFieldRutaArchivo.setText("");
        imageCatedral.setVisible(false);
        labelSeleccione.setLayoutX(173);
        labelSeleccione.setText("Seleccione en el menú lo que desee realizar");
        tablaReporte.setVisible(false);
        botonDescargar.setVisible(false);
        //Objetos de Registro
        labelArquidiocesis3.setVisible(false);
        registroLabelDescripcion.setVisible(false);
        registroLabelNombreParroquia.setVisible(false);
        txtFieldNombreParroquia.setVisible(false);
        registroLabelVicaria.setVisible(false);
        registroChoiceBoxVicaria.setVisible(false);
        registroLabelCiudadPertenece.setVisible(false);
        registroChoiceBoxCiudad.setVisible(false);
        registroLabelDireccionParroquia.setVisible(false);
        registrotextFieldDireccion.setVisible(false);
        registroLabelParroco.setVisible(false);
        registroComboBoxParroco.setVisible(false);
        registroLabelTelefono.setVisible(false);
        registroTxtFieldTelefono.setVisible(false);
        registroLabelSitioWeb.setVisible(false);
        registroTxtFieldSitioWeb.setVisible(false);
        registroLabelEmail.setVisible(false);
        registroTxtFieldEmail.setVisible(false);
        registroLabelFechaF.setVisible(false);
        registroDatePickerFF.setVisible(false);
        registroBotonEnviar.setVisible(false);
      registroChoiceBoxVicaria.getItems().addAll(cargarVicarias.cargarVicarias());
        registroChoiceBoxVicaria.getSelectionModel().selectFirst();
        registroChoiceBoxVicaria.getSelectionModel().selectedItemProperty().addListener((observable, valorAnterior, valorNuevo) -> {
            configurarCiudadSegunVicaria(valorNuevo.toString());});
        txtFieldNombreParroquia.setText("");
        registroComboBoxParroco.setValue(null);
        registrotextFieldDireccion.setText("");
        registroTxtFieldTelefono.setText("");
        registroTxtFieldSitioWeb.setText("");
        registroTxtFieldEmail.setText("");
        registroDatePickerFF.setValue(null);



        vicariaResponsable.setCellValueFactory(new PropertyValueFactory<>("vicariaResponsable"));
        proyectosTotales.setCellValueFactory(new PropertyValueFactory<>("proyectosTotales"));
        enEjecucion.setCellValueFactory(new PropertyValueFactory<>("enEjecucion"));
        planificados.setCellValueFactory(new PropertyValueFactory<>("planificados"));
        canceladosSuspendidos.setCellValueFactory(new PropertyValueFactory<>("canceladosSuspendidos"));
        presupuestoTotal.setCellValueFactory(new PropertyValueFactory<>("presupuestoTotal"));
        inversionPromedio.setCellValueFactory(new PropertyValueFactory<>("inversionPromedio"));
        proyectosTotales.setStyle("-fx-alignment: CENTER;");
        enEjecucion.setStyle("-fx-alignment: CENTER;");
        planificados.setStyle("-fx-alignment: CENTER;");
        canceladosSuspendidos.setStyle("-fx-alignment: CENTER;");
        presupuestoTotal.setStyle("-fx-alignment: CENTER-RIGHT;");
        inversionPromedio.setStyle("-fx-alignment: CENTER-RIGHT;");
        vicariaResponsable.setStyle("-fx-alignment: CENTER-LEFT;");

        try {
            cargarTabla();
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error si falla la conexión
        }
    }
    //Este metodo permite configurar el ChoiceBox de ciudad para que cambie de acuerdo a la vicaria seleccionada
    //Solo permite elección con la Vicaria Daule-Samborondón
    private void configurarCiudadSegunVicaria(String vicariaSeleccionada) {
        if (vicariaSeleccionada == null) return;

        switch (vicariaSeleccionada) {
            case "Vicaría Daule-Samborondón":
                // el choice box solo se activa con la vicaria Daule-Samborondón
                registroChoiceBoxCiudad.setDisable(false);
                registroChoiceBoxCiudad.setItems(FXCollections.observableArrayList("Daule", "Samborondón"));
                registroChoiceBoxCiudad.setValue(null);
                break;

            case "Vicaría Durán":
                bloquearYAsignarCiudad("Durán");
                break;
            case "Vicaría Santa Elena":
                bloquearYAsignarCiudad("Playas");
                break;
            default:
                // todas las demás pertenecen a Guayaquil
                bloquearYAsignarCiudad("Guayaquil");
                break;
        }
    }
//El metodo selecciona la ciudad una vez elegida la vicaria
    private void bloquearYAsignarCiudad(String nombreCiudad) {
        registroChoiceBoxCiudad.setItems(FXCollections.observableArrayList(nombreCiudad));
        registroChoiceBoxCiudad.setValue(nombreCiudad); // se selecciona una ciudad automaticamente
        registroChoiceBoxCiudad.setDisable(true);
    }


    @FXML
    void crearParroquia(ActionEvent event) {
        registroComboBoxParroco.getSelectionModel().selectFirst();
        labelSeleccione.setVisible(false);
        labelArquidiocesis2.setLayoutY(20);
        labelDinamico.setVisible(false);
        textFieldRutaArchivo.setVisible(false);
        botonExaminar.setVisible(false);
        botonSubir.setVisible(false);
        textFieldRutaArchivo.setText("");
        imageCatedral.setVisible(false);
        tablaReporte.setVisible(false);
        botonDescargar.setVisible(false);
        //Objetos de Registro
        labelArquidiocesis2.setVisible(false);
        labelArquidiocesis3.setVisible(true);
        registroLabelDescripcion.setLayoutY(63);
        registroLabelDescripcion.setLayoutX(263);
        registroLabelDescripcion.setText("Registrar parroquia en la Arquidiócesis");
        registroLabelDescripcion.setVisible(true);
        registroLabelNombreParroquia.setVisible(true);
        txtFieldNombreParroquia.setVisible(true);
        registroLabelVicaria.setVisible(true);
        registroChoiceBoxVicaria.setVisible(true);
        registroLabelCiudadPertenece.setVisible(true);
        registroChoiceBoxCiudad.setVisible(true);
        registroLabelDireccionParroquia.setVisible(true);
        registrotextFieldDireccion.setVisible(true);
        registroLabelParroco.setVisible(true);
        registroComboBoxParroco.setVisible(true);
        registroLabelTelefono.setVisible(true);
        registroTxtFieldTelefono.setVisible(true);
        registroLabelSitioWeb.setVisible(true);
        registroTxtFieldSitioWeb.setVisible(true);
        registroLabelEmail.setVisible(true);
        registroTxtFieldEmail.setVisible(true);
        registroLabelFechaF.setVisible(true);
        registroDatePickerFF.setVisible(true);
        registroBotonEnviar.setVisible(true);
    }

    @FXML
    void EnviarDatosParroquia(ActionEvent event) throws SQLException {
     //Validación de campos
        String nombreParroquia = txtFieldNombreParroquia.getText();
        int vicaria = registroChoiceBoxVicaria.getValue().getIdVicaria();
        String ciudad = registroChoiceBoxCiudad.getValue();
        String direccion = registrotextFieldDireccion.getText();
        int parroco = registroComboBoxParroco.getValue().getId_clerigo();
        String telefono = registroTxtFieldTelefono.getText();
        String sitioWeb = registroTxtFieldSitioWeb.getText();
        String email = registroTxtFieldEmail.getText();
        LocalDate fechaFundacion = registroDatePickerFF.getValue();

        if (fechaFundacion == null) {
            mostrarAlerta("Error", "Debe ingresar la fecha de fundacion.");
            return;
        }

        if (fechaFundacion.isAfter(LocalDate.now())) {
            mostrarAlerta(
                    "Fecha invalida",
                    "La fecha de fundacion no puede ser posterior a la fecha actual."
            );
            return;
        }
        if (nombreParroquia == null || nombreParroquia.trim().isEmpty()
                || ciudad == null || ciudad.trim().isEmpty()
                || direccion == null || direccion.trim().isEmpty()
                || fechaFundacion == null
        ) {
            mostrarAlerta("Error","Rellene los campos necesarios.");
            return;
        }
        if (telefono.trim().isEmpty()){
            telefono =null;
        }
        if (sitioWeb.trim().isEmpty()){
            sitioWeb=null;
        }
        if (email.trim().isEmpty()){
            email=null;
        }


        //Se va a validar los campos con formatos REGEX
        if (!Pattern.matches(RegexPatterns.NOMBRE_REGEX,nombreParroquia) ) {
            mostrarAlerta("Error","Formato invalido en campo Nombre de la Parroquia.");
            return;
        }

        if (!Pattern.matches(RegexPatterns.NOMBRE_REGEX,ciudad) ) {
            mostrarAlerta("Error","Formato invalido en campo Ciudad.");
            return;
        }
        if (!Pattern.matches(RegexPatterns.DIRECCION_REGEX,direccion) ) {
            mostrarAlerta("Error","Formato invalido en campo Dirección.");

            return;
        }


        if (telefono !=null && !Pattern.matches(RegexPatterns.TELEFONO_REGEX,telefono) ) {
            mostrarAlerta("Error","Formato invalido en campo Telefono. Deben ser 10 dígitos empezando en 09. Ej: 0912345678");
            return;
        }


        if (sitioWeb != null && !Pattern.matches(RegexPatterns.WEB_REGEX,sitioWeb) ) {
            mostrarAlerta("Error","Formato invalido en campo Sitio Web. Ej: www.iglesia.com");
            return;
        }



        if (email != null && !Pattern.matches(RegexPatterns.EMAIL_REGEX,email) ) {
            mostrarAlerta("Error","Formato invalido en campo Email. Ej: arquidiocesis@gmail.com");
            return;
        }

        registroBotonEnviar.setDisable(true);
        try {
            Parroquia parroquia = new Parroquia(nombreParroquia, vicaria, ciudad, direccion, fechaFundacion, parroco, telefono, email, sitioWeb);
            guardarParroquiaSQL.guardarEnSQL(parroquia);
            mostrarAlerta("Éxito", "La parroquia ha sido registrada correctamente.");
            refrescarCrearParroquia();

        } catch (Exception e) {
            mostrarAlerta("Error al guardar", "No se pudo registrar: " + e.getMessage());

        } finally {
            registroBotonEnviar.setDisable(false);
        }
    }
    //Metodo que permite refrescar los datos, para evitar que se mantengan los datos recién usados.
    // Es decir, una vez registrada la parroquia, limpia los datos para que nose pulse el boton enviar de manera indefinida.
    void refrescarCrearParroquia(){
        setRegistroComboBoxParroco();
        registroComboBoxParroco.getSelectionModel().selectFirst();
        txtFieldNombreParroquia.setText("");
        registroChoiceBoxVicaria.getSelectionModel().selectFirst();;
        registroChoiceBoxCiudad.setValue(null);
         registrotextFieldDireccion.setText("");
       registroTxtFieldTelefono.setText("");
        registroTxtFieldSitioWeb.setText("");
       registroTxtFieldEmail.setText("");
       registroDatePickerFF.setValue(null);

    }

    @FXML
    void generarReporte(ActionEvent event) throws SQLException {
        labelSeleccione.setVisible(false);
        labelArquidiocesis2.setLayoutY(24);
        labelDinamico.setVisible(false);
        textFieldRutaArchivo.setVisible(false);
        botonExaminar.setVisible(false);
        botonSubir.setVisible(false);
        textFieldRutaArchivo.setText("");
        imageCatedral.setVisible(false);
        tablaReporte.setVisible(true);
        botonDescargar.setVisible(true);
        //Objetos de Registro
        labelArquidiocesis2.setVisible(true);
        labelArquidiocesis3.setVisible(false);
        registroLabelDescripcion.setLayoutY(75);
        registroLabelDescripcion.setLayoutX(207);
        registroLabelDescripcion.setText("Pulse el botón descargar para obtener los reportes completos");
        registroLabelDescripcion.setVisible(true);
        registroLabelNombreParroquia.setVisible(false);
        txtFieldNombreParroquia.setVisible(false);
        registroLabelVicaria.setVisible(false);
        registroChoiceBoxVicaria.setVisible(false);
        registroLabelCiudadPertenece.setVisible(false);
        registroChoiceBoxCiudad.setVisible(false);
        registroLabelDireccionParroquia.setVisible(false);
        registrotextFieldDireccion.setVisible(false);
        registroLabelParroco.setVisible(false);
        registroComboBoxParroco.setVisible(false);
        registroLabelTelefono.setVisible(false);
        registroTxtFieldTelefono.setVisible(false);
        registroLabelSitioWeb.setVisible(false);
        registroTxtFieldSitioWeb.setVisible(false);
        registroLabelEmail.setVisible(false);
        registroTxtFieldEmail.setVisible(false);
        registroLabelFechaF.setVisible(false);
        registroDatePickerFF.setVisible(false);
        registroBotonEnviar.setVisible(false);
        cargarTabla();


    }
    @FXML
    void subirArchivosMenu(ActionEvent event) {
        labelSeleccione.setVisible(false);
        labelArquidiocesis2.setLayoutY(101);
        labelArquidiocesis2.setLayoutX(245);
        labelDinamico.setVisible(true);
        textFieldRutaArchivo.setVisible(true);
        botonExaminar.setVisible(true);
        botonSubir.setVisible(true);
        imageCatedral.setVisible(false);
        tablaReporte.setVisible(false);
        botonDescargar.setVisible(false);
//Objetos de Registro
        labelArquidiocesis2.setVisible(true);
        labelArquidiocesis3.setVisible(false);
        registroLabelDescripcion.setVisible(false);
        registroLabelNombreParroquia.setVisible(false);
        txtFieldNombreParroquia.setVisible(false);
        registroLabelVicaria.setVisible(false);
        registroChoiceBoxVicaria.setVisible(false);
        registroLabelCiudadPertenece.setVisible(false);
        registroChoiceBoxCiudad.setVisible(false);
        registroLabelDireccionParroquia.setVisible(false);
        registrotextFieldDireccion.setVisible(false);
        registroLabelParroco.setVisible(false);
        registroComboBoxParroco.setVisible(false);
        registroLabelTelefono.setVisible(false);
        registroTxtFieldTelefono.setVisible(false);
        registroLabelSitioWeb.setVisible(false);
        registroTxtFieldSitioWeb.setVisible(false);
        registroLabelEmail.setVisible(false);
        registroTxtFieldEmail.setVisible(false);
        registroLabelFechaF.setVisible(false);
        registroDatePickerFF.setVisible(false);
        registroBotonEnviar.setVisible(false);

    }
    @FXML
    void cambiarLabelExaminar(MouseEvent event) {
        labelDinamico.setText("Elegir ruta de archivo.");
        labelDinamico.setLayoutX(280);
    }
    @FXML
    void regresarLabelExaminar(MouseEvent event) {
        labelDinamico.setText("Elija el archivo a subir.");
        labelDinamico.setLayoutX(282);

    }

    @FXML
    void cambiarLabelSubir(MouseEvent event) {
        labelDinamico.setText("Subir archivos.");
        labelDinamico.setLayoutX(302);

    }

    @FXML
    void regresarLabelSubir(MouseEvent event) {
        labelDinamico.setText("Elija el archivo a subir.");
        labelDinamico.setLayoutX(282);

    }

    private void cargarTabla() throws SQLException{
            // lista observable donde se reciben los datos
            ObservableList<VistaReporte> vistaReporteObservableList;
            vistaReporteObservableList = cargarVistaReporte.cargarReporte();
            tablaReporte.setItems(vistaReporteObservableList);
        }


    @FXML
    void examinarRuta(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de Arquidiócesis");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Todos los archivos soportados", "*.csv","*.sql"),
        new FileChooser.ExtensionFilter("Archivos CSV (*.csv)",       "*.csv"),
        new FileChooser.ExtensionFilter("Archivos SQL (*.sql)",       "*.sql")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File archivoTemporal = fileChooser.showOpenDialog(stage);

        if (archivoTemporal != null) {
            String nombreArchivo = archivoTemporal.getName().toLowerCase();

            // 2. Verificamos si termina en .csv
            if (!nombreArchivo.endsWith(".csv") && !nombreArchivo.endsWith(".sql")) {
                mostrarAlerta("Formato Incorrecto", "El archivo seleccionado no es un CSV (.csv) o SQL (.sql). Por favor, elige el archivo correcto.");
                archivoSeleccionado = null;
                textFieldRutaArchivo.clear();
                return;
            }

            archivoSeleccionado = archivoTemporal;
            textFieldRutaArchivo.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    @FXML
    void subirArchivo(ActionEvent event) {
        if (archivoSeleccionado == null) {
            mostrarAlerta("Error", "No hay archivo seleccionado");
            return;
        }

        String nombreArchivo = archivoSeleccionado.getName().toLowerCase().replace(".csv", "");

        try {

            if(nombreArchivo.endsWith(".sql")){
                importarSQL.restaurarBaseDatos(archivoSeleccionado);
                mostrarAlerta("Éxito", "La base ha sido restaurada correctamente.");
            } else if (nombreArchivo.endsWith(".csv")) {

                switch (nombreArchivo.toLowerCase()) {

                    case "actividades":
                        importarActividadesCSV.importarActividades(archivoSeleccionado);
                        break;
                    case "parroquia":
                        importarParroquiasCSV.importarParroquias(archivoSeleccionado);
                        break;
                    case "clerigo":
                        importarClerigoCSV.importarClerigo(archivoSeleccionado);
                        break;

                    case "convenio":
                        importarConvenioCSV.importarConvenio(archivoSeleccionado);
                        break;

                    case "evento":
                        importarEventosCSV.importarEventos(archivoSeleccionado);
                        break;

                    case "lugares_culto":
                        importarLugaresCultoCSV.importarLugaresCulto(archivoSeleccionado);
                        break;

                    case "pastorales":
                        importarPastoralesCSV.importarPastorales(archivoSeleccionado);
                        break;

                    case "proyectos_pastorales":
                        importarProyectosPastoralesCSV.importarProyectosPastorales(archivoSeleccionado);
                        break;

                    case "receptor_sacramento":
                        importarReceptorSacramentoCSV.importarReceptorSacramento(archivoSeleccionado);
                        break;

                    case "registro_sacramento":
                        importarRegistroSacramentoCSV.importarRegistroSacramento(archivoSeleccionado);
                        break;

                    case "responsable":
                        importarResponsableCSV.importarResponsable(archivoSeleccionado);
                        break;

                    case "vicaria":
                        importarVicariasCSV.importarVicarias(archivoSeleccionado);
                        break;

                    default:
                        mostrarAlerta("Archivo Desconocido",
                                "El archivo '" + nombreArchivo + "' no corresponde a ninguna tabla configurada.");
                        return;
                }
                mostrarAlerta("Éxito", "Importación de " + nombreArchivo + " completada correctamente.");
            }
            textFieldRutaArchivo.clear();
            archivoSeleccionado = null;

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Falló la importación: " + e.getMessage());
        }
    }

    @FXML
    void descargarPDF(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo PDF");
        fileChooser.setInitialFileName("ReportesArquidiocesis.pdf"); // Nombre por defecto

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        File fileDestino = fileChooser.showSaveDialog(stage);

        if (fileDestino != null) {
            guardarArchivo(fileDestino);
        }
    }




//     Método auxiliar para mostrar alertas en JavaFX
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}










