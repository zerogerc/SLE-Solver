package tools.ui.components;

import com.sun.istack.internal.NotNull;
import tools.logic.Equation;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Double.valueOf;
import static tools.Constants.MATRIX_SIZE;
import static tools.Constants.RESOURCES_DIR;

public class MatrixComponent extends UIComponent {

    @NotNull
    private static final Color LIGHT_GRAY = new Color(232, 232, 232);

    @NotNull
    private final Table table;

    @NotNull
    private static final DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(column == MATRIX_SIZE ? Color.PINK : Color.WHITE);
            c.setForeground(column == MATRIX_SIZE ? Color.WHITE : Color.BLACK);
            return c;
        }
    };

    private MatrixComponent(@NotNull Component component, @NotNull Table table) {
        super(component);
        this.table = table;
    }

    @NotNull
    public void setMatrix(@NotNull double[][] matrix) {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE + 1; j++) {
                table.setValueAt(matrix[i][j], i, j);
            }
        }
    }

    @NotNull
    public Equation getEquation() {
        double[][] rowData = table.data;

        double[][] matrix = new double[MATRIX_SIZE][MATRIX_SIZE];
        double[] vector = new double[MATRIX_SIZE];

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE + 1; j++) {
                if (j == MATRIX_SIZE) {
                    vector[i] = rowData[i][j];
                } else {
                    matrix[i][j] = rowData[i][j];
                }
            }
        }

        return new Equation(matrix, vector);
    }

    @NotNull
    public static MatrixComponent createMatrix() {
        Table dataModel = new Table(MATRIX_SIZE, MATRIX_SIZE + 1);

        JTable table = new JTable(dataModel) {

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                return cellRenderer;
            }

            @Override
            public Component prepareEditor(TableCellEditor editor, int row, int column) {
                Component c = super.prepareEditor(editor, row, column);
                if (c instanceof JTextComponent) {
                    ((JTextComponent) c).selectAll();
                }
                return c;
            }
        };
        table.setRowHeight(20);

        table.setTableHeader(null);
        table.setCellSelectionEnabled(true);
        table.setGridColor(LIGHT_GRAY);
        table.setBorder(new MatteBorder(1, 1, 0, 0, LIGHT_GRAY));

        JScrollPane scrollpane = new JScrollPane(table) {
            @Override
            public Insets getInsets() {
                return new Insets(10, 10, 10, 10);
            }
        };

        return new MatrixComponent(scrollpane, dataModel);
    }

    private static class Table extends AbstractTableModel {

        final int rows;

        final int columns;

        @NotNull
        final double[][] data;

        public Table(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
            this.data = new double[rows][columns];
            fillWithData();
        }



        public int getRowCount() {
            return rows;
        }

        public int getColumnCount() {
            return columns;
        }

        public boolean isCellEditable(int row, int col) {
            return true;
        }

        @NotNull
        public Double getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setValueAt(@NotNull Object value, int row, int col) {
            try {
                if (value instanceof Double) {
                    data[row][col] = (Double) value;
                } else if (value instanceof String) {
                    Double newValue = Double.parseDouble(((String) value));
                    data[row][col] = newValue;
                }
            } catch (NumberFormatException e) {
                // TODO: do something
            } finally {
                int num = (int) (data[row][col] * 1000);
                data[row][col] = (double) num / 1000d;
                fireTableCellUpdated(row, col);
            }
        }

        private void fillWithData() {
            try {
                Scanner scanner = new Scanner(new File(RESOURCES_DIR + "matrix"));

                for (int i = 0; i < MATRIX_SIZE; i++) {
                    for (int j = 0; j < MATRIX_SIZE + 1; j++) {
                        if (scanner.hasNextDouble()) {
                            data[i][j] = scanner.nextDouble();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Could not open file with matrix!");
                fillWithDefault();
            }
        }

        private void fillWithDefault() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    data[i][j] = valueOf(i + j);
                }
            }
        }
    }
}
