package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.CellType;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelObserver;
import com.comp301.a09akari.model.Puzzle;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class PuzzleView implements FXComponent, ModelObserver {
  private final Model model;
  private final ClassicMvcController controller;

  public PuzzleView(Model model, ClassicMvcController controller) {
    if (model != null && controller != null) {
      this.model = model;
      this.controller = controller;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Parent render() {
    // setting grid & puzzle
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(0, 90, 45, 90));
    grid.setGridLinesVisible(true);
    grid.getChildren().clear();
    Puzzle p = model.getActivePuzzle();

    for (int r = 0; r < p.getHeight(); r++) {
      for (int c = 0; c < p.getWidth(); c++) {
        Image light = new Image("light-bulb.png");
        ImageView Lview = new ImageView(light);
        Lview.setFitHeight(10);
        Lview.setFitWidth(8);
        Image redlight = new Image("red-light.png");
        ImageView RLview = new ImageView(redlight);
        RLview.setFitHeight(9);
        RLview.setFitWidth(7);

        int finalR = r;
        int finalC = c;
        // diff buttons for diff cell types!
        Button button = new Button();
        if (p.getCellType(r, c) == CellType.CORRIDOR) {
          if (model.isLamp(r, c)) {
            if (model.isLampIllegal(r, c)) {
              // illegal lamp style
              button.setId("ILbutton");
              RLview.setFitWidth(30);
              RLview.setFitHeight(30);
              RLview.setPreserveRatio(true);
              button.setGraphic(RLview);
            } else {
              // legal lamp style
              button.setId("LLbutton");
              Lview.setFitWidth(20);
              Lview.setFitHeight(20);
              Lview.setPreserveRatio(true);
              button.setGraphic(Lview);
            }
            grid.add(button, c, r);
          } else {
            // not a lamp; empty corridor
            if (model.isLit(r, c)) {
              // lit corridor button
              button.setId("Litbutton");
              button.setMaxWidth(10);
              button.setMaxHeight(10);
            } else {
              // regular unlit button
              button.setId("Unlitbutton");
              button.setMaxWidth(10);
              button.setMaxHeight(10);
            }
            grid.add(button, c, r);
          }
          // setting on action new lamp in cell is either legal or illegal
          button.setOnAction(
              event -> {
                controller.clickCell(finalR, finalC);
              });
        }
        // no buttons below here!
        if (p.getCellType(r, c) == CellType.CLUE) {
          if (model.isClueSatisfied(r, c)) {
            Rectangle Sclue = new Rectangle(c, r, 10, 10);
            Sclue.setFill(Color.GREEN);
            Sclue.setWidth(35);
            Sclue.setHeight(30);
            grid.add(Sclue, c, r);
            // solved clue style
          } else {
            String s = Integer.toString((p.getClue(finalR, finalC)));
            Rectangle clue = new Rectangle(c, r, 10, 10);
            clue.setWidth(35);
            clue.setHeight(30);

            grid.add(clue, c, r);
            Label label = new Label(s);
            label.setId("ClueText");
            label.setTextFill(Color.WHITE);
            label.setAlignment(Pos.CENTER);
            label.setContentDisplay(ContentDisplay.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            grid.setCenterShape(true);
            grid.setAlignment(Pos.CENTER);
            grid.add(label, c, r);
            // regular clue style
          }
        }
        if (p.getCellType(r, c) == CellType.WALL) {
          Rectangle wall = new Rectangle(c, r, 10, 10);
          wall.setWidth(35);
          wall.setHeight(30);
          grid.add(wall, c, r);
          // wall style
        }
      }
    }
    return grid;
  }

  @Override
  public void update(Model model) {}
}
