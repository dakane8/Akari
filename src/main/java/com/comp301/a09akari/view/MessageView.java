package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelObserver;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MessageView implements FXComponent, ModelObserver {
  private final Model model;
  private final ClassicMvcController controller;

  public MessageView(Model model, ClassicMvcController controller) {
    if (model != null && controller != null) {
      this.model = model;
      this.controller = controller;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Parent render() {
    VBox pane = new VBox();
    pane.getChildren().clear();
    pane.setAlignment(Pos.CENTER);
    Label title = new Label("Akari");
    title.setFont(new Font("Arial", 40));
    Label PuzzleIndex = new Label("Puzzles Available: " + model.getPuzzleLibrarySize());
    PuzzleIndex.setId("PuzzleIndex");
    Label PuzzleSize = new Label("Current Puzzle: " + ((model.getActivePuzzleIndex() + 1)));
    PuzzleSize.setFont(new Font("Arial", 10));
    PuzzleIndex.setFont(new Font("Arial", 10));
    pane.getChildren().add(title);
    pane.getChildren().add(PuzzleSize);
    pane.getChildren().add(PuzzleIndex);
    Label win = new Label("You win! Congrats!");
    win.setFont(new Font("Arial", 30));
    win.setId("win");
    if (model.isSolved()) {
      pane.getChildren().add(win);
    }
    return pane;
  }

  // ignore for now
  @Override
  public void update(Model model) {}
}
