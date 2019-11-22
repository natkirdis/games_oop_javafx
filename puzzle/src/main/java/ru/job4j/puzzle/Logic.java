package ru.job4j.puzzle;

import ru.job4j.puzzle.firuges.Cell;
import ru.job4j.puzzle.firuges.Figure;

import java.util.Arrays;

/**
 * Логика игры
 *
 * @author Petr Arsentev (parsentev@yandex.ru), Natalia Kirdis (kirdisnatalia@gmail.com)
 * @version 1.1
 * @since 21.11.2019
 */
public class Logic {
  /**
   * Размер игрового поля
   */
  private final int size;
  /**
   * Игоровое поле
   */
  private final Figure[] figures;
  private int index = 0;

  /**
   * Конструктор - создание нового объекта на основе размера игрового поля
   *
   * @param size - размер поля
   */
  public Logic(int size) {
    this.size = size;
    this.figures = new Figure[size * size];
  }

  public void add(Figure figure) {
    this.figures[this.index++] = figure;
  }

  public boolean move(Cell source, Cell dest) {
    boolean rst = false;
    int index = this.findBy(source);
    if (index != -1) {
      Cell[] steps = this.figures[index].way(source, dest);
      if (this.isFree(steps)) {
        rst = true;
        this.figures[index] = this.figures[index].copy(dest);
      }
    }
    return rst;
  }

  /**
   * Функция проверяет свободна ли ячейка {@link Cell}
   *
   * @param cells - ячейки
   * @return возвращает свободна ли ячейка
   */
  public boolean isFree(Cell... cells) {
    boolean result = cells.length > 0;
    for (Cell cell : cells) {
      if (this.findBy(cell) != -1) {
        result = false;
        break;
      }
    }
    return result;
  }

  /**
   * Функция очищает ячайки {@link Logic@position}
   */
  public void clean() {
    for (int position = 0; position != this.figures.length; position++) {
      this.figures[position] = null;
    }
    this.index = 0;
  }

  /**
   * Функция ищет ячайку
   *
   * @param cell - ячейка
   * @return возвращает индекс искомой ячейки
   */
  private int findBy(Cell cell) {
    int rst = -1;
    for (int index = 0; index != this.figures.length; index++) {
      if (this.figures[index] != null && this.figures[index].position().equals(cell)) {
        rst = index;
        break;
      }
    }
    return rst;
  }

  /**
   * Функция проверки на победу
   *
   * @return возвращает есть ли победитель в игре
   */
  public boolean isWin() {
    int[][] table = this.convert();
    boolean result = false;

    for (int row = 0; row != table.length && !result; row++) {
      int cells = 0;
      int rows = 0;
      for (int cell = 0; cell != table.length; cell++) {
        if (table[row][cell] == 1) {
          rows++;
        }
        if (table[cell][row] == 1) {
          cells++;
        }
      }
      result = cells == table.length || rows == table.length;
    }

    return result;
  }

  /**
   * Функция проверки на победу
   *
   * @return преобразовывает поле в двумерный массив int[][]. Записывается значение 1, если в ячейке элемент пазла(круг).
   */
  public int[][] convert() {
    int[][] table = new int[this.size][this.size];
    for (int row = 0; row != table.length; row++) {
      for (int cell = 0; cell != table.length; cell++) {
        int position = this.findBy(new Cell(row, cell));
        if (position != -1 && this.figures[position].movable()) {
          table[row][cell] = 1;
        }
      }
    }
    return table;
  }

  @Override
  public String toString() {
    return Arrays.toString(this.convert());
  }
}
