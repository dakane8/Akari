package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelObserver;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class MainView implements FXComponent, ModelObserver {
  private final FXComponent PuzzleView;
  private final FXComponent ControlView;
  private final FXComponent MessageView;
  private final Scene scene;

  public MainView(Model model, ClassicMvcController controller) {
    this.PuzzleView = new PuzzleView(model, controller);
    this.ControlView = new ControlView(model, controller);
    this.MessageView = new MessageView(model, controller);
    this.scene = new Scene(render());
    scene.getStylesheets().add("main.css");
    model.addObserver(this);
  }

  @Override
  public void update(Model model) {
    scene.setRoot(render());
  }

  public Scene getScene() {
    return scene;
  }

  @Override
  public Parent render() {
    BorderPane pane = new BorderPane();
    pane.setTop(MessageView.render());
    pane.setBottom(ControlView.render());
    pane.setCenter(PuzzleView.render());
    return pane;
  }
}
