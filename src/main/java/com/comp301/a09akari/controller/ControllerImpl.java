package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.*;

import java.util.Random;

public class ControllerImpl implements ClassicMvcController {

  private Model model;

  public ControllerImpl(Model model) {
    if (model != null) {
      this.model = model;
    }
  }

  @Override
  public void clickNextPuzzle() {
    int i = model.getActivePuzzleIndex();
    if (i + 1 > model.getPuzzleLibrarySize()) {
      model.setActivePuzzleIndex(i);
    } else {
      model.setActivePuzzleIndex(model.getActivePuzzleIndex() + 1);
    }
  }

  @Override
  public void clickPrevPuzzle() {
    int i = model.getActivePuzzleIndex();
    if (i - 1 < 0) {
      model.setActivePuzzleIndex(i);
    } else {
      model.setActivePuzzleIndex(model.getActivePuzzleIndex() - 1);
    }
  }

  @Override
  public void clickRandPuzzle() {
    Random random = new Random();
    int i = random.nextInt(model.getPuzzleLibrarySize());
    while (i == model.getActivePuzzleIndex()) {
      i = random.nextInt(model.getPuzzleLibrarySize());
    }
    model.setActivePuzzleIndex(i);
  }

  @Override
  public void clickResetPuzzle() {
    model.resetPuzzle();
  }

  @Override
  public void clickCell(int r, int c) {
    if (model.isLamp(r, c)) {
      model.removeLamp(r, c);
    } else {
      model.addLamp(r, c);
    }
  }
}
