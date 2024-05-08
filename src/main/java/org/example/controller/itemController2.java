package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.example.entity.Test;
import org.example.interfaces.MyListener2;

public class itemController2 {

    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;
    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener2.onClickListener(fruit);
    }

    private Test fruit;
    private MyListener2 myListener2;

    public void setData(Test fruit, MyListener2 myListener2) {
        this.fruit = fruit;
        this.myListener2 = myListener2;
        nameLabel.setText(Integer.toString(fruit.getId()));
        priceLable.setText(Integer.toString(fruit.getScore()));

    }
}
