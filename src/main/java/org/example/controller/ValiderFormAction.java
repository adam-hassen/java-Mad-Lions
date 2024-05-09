package org.example.controller;

import EDU.userjava1.controllers.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.entity.Action;
import org.example.entity.ActionLocation;
import org.example.entity.TypeName;
import org.example.service.ActionService;
import org.example.service.LocationService;
import org.example.service.TypeNameService;
import org.jfree.data.json.impl.JSONObject;


import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
public class ValiderFormAction {
    @FXML
    public ComboBox<TypeName> Type;
    @FXML
    public TextField Description;
    @FXML
    public DatePicker Date;
    @FXML
    public TextField Quantite;
    @FXML
    public Button ModifierAction;
    @FXML
    public Button ValiderAction;
    @FXML
    public ComboBox<Integer>  secondComboBox;
    @FXML
    public ComboBox<Integer> minuteComboBox;
    @FXML
    public ComboBox<Integer> hourComboBox;
    @FXML
    public ToggleGroup toggleGroup;
    @FXML
    public ToggleButton timeToggle;
    @FXML
    public ToggleButton quantiteToggle;
    @FXML
    private Label timeLabel;
    @FXML
    private Label quantiteLabel;
    private ActionService query;
    private TypeNameService query2;
    private LocationService query3;
    private int mod;
    private int userId;
    private ActionLocation loc;
    @FXML
    private VBox vboxside;
    @FXML
    public void initialize() {
        query2 = new TypeNameService();
        query = new ActionService();
        query3 = new LocationService();
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        hourComboBox.setItems(hours);
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            minutes.add(i);
        }
        minuteComboBox.setItems(minutes);
        ObservableList<Integer> seconds = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            seconds.add(i);
        }
        secondComboBox.setItems(seconds);
        ValiderAction.setOnAction(this::ValiderForm);
        ModifierAction.setOnAction(this::handleModifierAct);
        showtypes();
        //toggle quantite
        toggleGroup = new ToggleGroup();
        timeToggle.setToggleGroup(toggleGroup);
        quantiteToggle.setToggleGroup(toggleGroup);
        // reset show quantity type
        timeLabel.setVisible(false);
        quantiteToggle.setVisible(false);
        timeToggle.setVisible(false);
        hourComboBox.setVisible(false);
        minuteComboBox.setVisible(false);
        secondComboBox.setVisible(false);
        quantiteLabel.setVisible(true);
        Quantite.setVisible(true);
        /*if (isUpdatePage) {
            ModifierAction.setVisible(true);
            ValiderAction.setVisible(false);
        } else {
            ModifierAction.setVisible(false);
            ValiderAction.setVisible(true);
        }*/
    }
    public void showtypes(){
        ObservableList<TypeName> types = FXCollections.observableArrayList(query2.afficherTypeName());;
        Type.setItems(types);
        // sssssssss
        Type.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getMateriel().equals("temps")) {
                    timeLabel.setVisible(true);
                    hourComboBox.setVisible(true);
                    minuteComboBox.setVisible(true);
                    secondComboBox.setVisible(true);
                    quantiteLabel.setVisible(false);
                    Quantite.setVisible(false);
                } else if (newValue.getMateriel().equals("solid")) {
                    timeLabel.setVisible(false);
                    hourComboBox.setVisible(false);
                    minuteComboBox.setVisible(false);
                    secondComboBox.setVisible(false);
                    quantiteLabel.setVisible(true);
                    Quantite.setVisible(true);
                }
            }
        });
    }
    @FXML
    private void handleToggle() {
        ToggleButton selectedToggle = (ToggleButton) toggleGroup.getSelectedToggle();
        if (selectedToggle == timeToggle) {
            Quantite.setText("0");
            timeLabel.setVisible(true);
            hourComboBox.setVisible(true);
            minuteComboBox.setVisible(true);
            secondComboBox.setVisible(true);
            quantiteLabel.setVisible(false);
            Quantite.setVisible(false);
        } else if (selectedToggle == quantiteToggle) {
            hourComboBox.setValue(0);
            minuteComboBox.setValue(0);
            secondComboBox.setValue(0);
            timeLabel.setVisible(false);
            hourComboBox.setVisible(false);
            minuteComboBox.setVisible(false);
            secondComboBox.setVisible(false);
            quantiteLabel.setVisible(true);
            Quantite.setVisible(true);
        }
        //toggle metric type
        TypeName selectedType = Type.getSelectionModel().getSelectedItem();
        if (selectedType != null) {
            if (selectedType.getMateriel().equals("temps")) {
                timeLabel.setVisible(true);
                hourComboBox.setVisible(true);
                minuteComboBox.setVisible(true);
                secondComboBox.setVisible(true);
                quantiteLabel.setVisible(false);
                Quantite.setVisible(false);
            } else if (selectedType.getMateriel().equals("solid")) {
                timeLabel.setVisible(false);
                hourComboBox.setVisible(false);
                minuteComboBox.setVisible(false);
                secondComboBox.setVisible(false);
                quantiteLabel.setVisible(true);
                Quantite.setVisible(true);
            }
        }
    }
    public void ModifierForm(Action action){
            mod=action.getId();
            userId=action.getUser_id();
            loc=action.getLocation_id();
            Type.setValue(action.getType_id());
            Description.setText(action.getDescription());
            Date.setValue(action.getDate());
            if (action.getType_id().getMateriel().equals("temps") ) {
                String[] timeParts = action.getQuantite_time().split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                int second = Integer.parseInt(timeParts[2]);
                hourComboBox.setValue(hour);
                minuteComboBox.setValue(minute);
                secondComboBox.setValue(second);
                timeToggle.setSelected(true);
                quantiteToggle.setSelected(false);
                timeLabel.setVisible(true);
                hourComboBox.setVisible(true);
                minuteComboBox.setVisible(true);
                secondComboBox.setVisible(true);
                quantiteLabel.setVisible(false);
                Quantite.setVisible(false);
            } else {
                timeToggle.setSelected(false);
                quantiteToggle.setSelected(true);
                timeLabel.setVisible(false);
                hourComboBox.setVisible(false);
                minuteComboBox.setVisible(false);
                secondComboBox.setVisible(false);
                quantiteLabel.setVisible(true);
                Quantite.setVisible(true);
                Quantite.setText(Double.toString(action.getQuantite()));
            }
            Description.setText(action.getDescription());

    }
    public void ValiderForm(ActionEvent event) {
        int hour = hourComboBox.getValue() != null ? hourComboBox.getValue() : 0;
        int minute = minuteComboBox.getValue() != null ? minuteComboBox.getValue() : 0;
        int second = secondComboBox.getValue() != null ? secondComboBox.getValue() : 0;
        String time = String.format("%02d:%02d:%02d", hour, minute, second);

        if (Type.getValue() == null || Date.getValue() == null) {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Veuillez sélectionner le type et la date.");
            successAlert.showAndWait();
            return;
        }

        Action act = new Action(Type.getValue(), 0.0, Date.getValue(), Description.getText(), time);
        act = query.calculerScoreEtDanger(act);
        act.setUser_id(Login.v.getId());

        if (!Pattern.matches("[a-zA-Z0-9\\s]*", Description.getText())) {
            Alert validationAlert = new Alert(Alert.AlertType.ERROR);
            validationAlert.setTitle("Gestion De Consommation :");
            validationAlert.setHeaderText(null);
            validationAlert.setContentText("La description ne doit contenir que des lettres, des nombres et des espaces !");
            validationAlert.showAndWait();
            return;
        }

        if (!Quantite.getText().isEmpty()) {
            try {
                double quantite = Double.parseDouble(Quantite.getText());

                if (quantite <= 0) {
                    throw new NumberFormatException();
                }

                act.setQuantite(quantite);
                act.setQuantite_time("00:00:00");
            } catch (NumberFormatException e) {
                Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                validationAlert.setTitle("Gestion Consommation :");
                validationAlert.setHeaderText(null);
                validationAlert.setContentText("La quantité doit contenir uniquement des nombres strictement positifs !");
                validationAlert.showAndWait();
                return;
            }
        } else {
            act.setQuantite(0.0);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir ajouter cette action ?");
        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {
            act=query.calculerScoreEtDanger(act);
                //String ip = "144.2.200.163";
                //act.setLocation_id(LocateUser(ip));
                //System.out.println(act.getLocation_id());
            //query3.ajouterActionLocation(act.getLocation_id());
            act.setLocation_id(query3.chercherLocation());
            System.out.println(act);
            query.ajouterAction(act);
            //UserServices GS = new UserServices();
            String ListeDanger=null;
            ListeDanger=query.ListeDanger(Login.v);
            System.out.println(ListeDanger);
            double dang = query.moyenneDanger(Login.v.getId());
            String danger="";
            if (dang < 2) {
                danger="Vous ne présentez pas de danger";
            } else if (dang <= 4) {
                danger="Niveau de danger moyen";
            } else {
                danger="Vous êtes considéré un danger !";
            }
            if (ListeDanger != null) {
                try {
                    query.sendEmail(Login.v.getUsername(), "Alerte : Dépassement du seuil de danger pour l'environnement",
                            "Cher/Chère "+ Login.v.getName()+" ,\n\n" +
                                    "Nos systèmes de surveillance ont détecté une augmentation significative des indicateurs environnementaux, indiquant un dépassement du seuil de danger.\n\n" +
                                    "Nous vous exhortons à agir immédiatement pour faire face à cette situation cruciale. Il est impératif que nous trouvions ensemble des solutions viables afin d'atténuer les défis environnementaux auxquels nous sommes confrontés.\n\n" +
                                    "Merci pour votre attention à cette demande. Nous attendons votre réponse et votre collaboration.\n\n" +
                                    ListeDanger+ "\n"+
                                    danger+"\n"+
                                    "Cordialement,\n" +
                                    "EcoGardien");
                    System.out.println("Email sent successfully.");
                } catch (MessagingException e) {
                    System.out.println("Failed to send email: " + e.getMessage());
                }
                // End email
            }

            List<Action> actionList = query.afficherActions(Login.v.getId());
            ObservableList<Action> observableList = FXCollections.observableArrayList(actionList);
        }
    }
    public void handleModifierAct(ActionEvent event){
        int hour = 0;
        if (hourComboBox.getValue() != null) {
            hour=hourComboBox.getValue();
        }
        int minute = 0;
        if (minuteComboBox.getValue() != null) {
            minute=minuteComboBox.getValue();
        }
        int second = 0;
        if (secondComboBox.getValue() != null) {
            second=secondComboBox.getValue();
        }
        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        Double k = 0.0;
        if ((!Quantite.getText().isEmpty() || time!=null) && (Type.getValue()!=null) && (Date.getValue()!=null)) {
            Action act = new Action(Type.getValue(), k, Date.getValue(), Description.getText(), time);
            act = query.calculerScoreEtDanger(act);
            act.setId(mod);
            act.setUser_id(Login.v.getId());
            act.setLocation_id(loc);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir modifier cette action?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                double quantite = 0.0;
                if (!Pattern.matches("^[a-zA-Z0-9\\s]+$", Description.getText())) {
                    Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                    validationAlert.setTitle("Gestion De Consommation :");
                    validationAlert.setHeaderText(null);
                    validationAlert.setContentText("Description doit contenir que des lettes, des nombres et des escpaces!");
                    validationAlert.showAndWait();
                    return;
                }
                else if (!Quantite.getText().isEmpty()) {
                    try {
                        quantite = Double.parseDouble(Quantite.getText());
                        if (quantite <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        Alert validationAlert = new Alert(Alert.AlertType.ERROR);

                        validationAlert.setTitle("Gestion Consommation :");
                        validationAlert.setHeaderText(null);
                        validationAlert.setContentText("Quantite dois contenir que des nombres strictement positives!");
                        validationAlert.showAndWait();
                        return;
                    }
                }
                    query.modifierAction(act.getId(), act);
               /* try {
                    //containerView.getChildren().clear();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/showActions.fxml"));
                    Parent root = loader.load();
                    showActions showactioncontroller = loader.getController();
                    //Pane newContent = loader.load();
                    showactioncontroller.showAction(root);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Gestion De Consommation Alert!");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Action modifiée avec succès!");
                    successAlert.showAndWait();
                    List<Action> actionList = query.afficherActions(Login.v.getId());
            }
        }
        if (Date.getValue()==null){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Date Null ");
            successAlert.showAndWait();
        }
        if (Quantite.getText().isEmpty() && time.equals("00:00:00")){
            if (Quantite.getText().isEmpty()) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Gestion De Consommation Alert!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Quantite Null");
                successAlert.showAndWait();
            }
            if (time.equals("00:00:00")){
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Gestion De Consommation Alert!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Time Null");
                successAlert.showAndWait();
            }
        }
        if (Type.getValue()==null){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Type Null");
            successAlert.showAndWait();
        }
    }
        public ActionLocation LocateUser(String ip) {
            String baseUrl = "https://ipapi.co/";

            Request latitudeRequest = new Request.Builder()
                    .url(baseUrl + ip + "/latitude/")
                    .build();
            Request longitudeRequest = new Request.Builder()
                    .url(baseUrl + ip + "/longitude/")
                    .build();
            Request countryRequest = new Request.Builder()
                    .url(baseUrl + ip + "/country_name/")
                    .build();
            Request regionRequest = new Request.Builder()
                    .url(baseUrl + ip + "/region/")
                    .build();

            OkHttpClient client = new OkHttpClient();
            try (Response latitudeResponse = client.newCall(latitudeRequest).execute();
                 Response longitudeResponse = client.newCall(longitudeRequest).execute();
                 Response countryResponse = client.newCall(countryRequest).execute();
                 Response regionResponse = client.newCall(regionRequest).execute();
            )
            {

                int latitudeCode = latitudeResponse.code();
                int longitudeCode = longitudeResponse.code();
                int countryCode = countryResponse.code();
                int regionCode = regionResponse.code();

                if (!latitudeResponse.isSuccessful() || !longitudeResponse.isSuccessful() || !countryResponse.isSuccessful() ||
                        !regionResponse.isSuccessful()) {
                    System.err.println("Unexpected code for latitude: " + latitudeCode);
                    System.err.println("Unexpected code for longitude: " + longitudeCode);
                    System.err.println("Unexpected code for country: " + countryCode);
                    System.err.println("Unexpected code for region: " + regionCode);

                    throw new IOException("Unexpected code");
                }

                String latitude = latitudeResponse.body().string();
                String longitude = longitudeResponse.body().string();
                String country = countryResponse.body().string();
                String region = regionResponse.body().string();
                // String postal = postalResponse.body().string();

                String address = region + ", " + country;
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                System.out.println("Address: " + address);
                System.out.println("Region: " + region);
                ActionLocation loc = new ActionLocation("test",address,latitude,longitude);
                System.out.println(loc);
            return loc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        // end ipapi api
    }

}
