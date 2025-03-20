package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelObserver;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControlView implements FXComponent, ModelObserver {
  private final Model model;
  private final ClassicMvcController controller;
  private Button prev;
  private Button next;
  private Button rand;
  private Button reset;

  public ControlView(Model model, ClassicMvcController controller) {
    if (model != null && controller != null) {
      this.model = model;
      this.controller = controller;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Parent render() {
    // creating vertical layout
    VBox mpane = new VBox(10);
    mpane.getChildren().clear();
    mpane.setAlignment(Pos.CENTER);
    // creating horizontal layouts to go inside vertical
    HBox pane1 = new HBox(10);
    pane1.getChildren().clear();
    pane1.setAlignment(Pos.CENTER);
    HBox pane2 = new HBox(10);
    pane2.getChildren().clear();
    pane2.setAlignment(Pos.CENTER);
    // alignment^

    if (prev == null) {
      prev = new Button("Previous puzzle");
      prev.setOnAction((ActionEvent event) -> controller.clickPrevPuzzle());
      prev.setId("prev");
    }
    if (next == null) {
      next = new Button("Next puzzle");
      next.setOnAction((ActionEvent event) -> controller.clickNextPuzzle());
      next.setId("next");
    }
    if (rand == null) {
      rand = new Button("Random puzzle");
      rand.setOnAction((ActionEvent event) -> controller.clickRandPuzzle());
      rand.setId("rand");
    }
    if (reset == null) {
      reset = new Button("Reset puzzle");
      reset.setOnAction((ActionEvent event) -> controller.clickResetPuzzle());
      reset.setId("reset");
    }
    mpane.getChildren().add(pane1);
    mpane.getChildren().add(pane2);
    pane1.getChildren().add(prev);
    pane1.getChildren().add(next);
    pane2.getChildren().add(rand);
    pane2.getChildren().add(reset);
    return mpane;
  }

  // ignore
  @Override
  public void update(Model model) {}
}
