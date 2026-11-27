package ui;

import java.util.function.IntConsumer;
import java.awt.*;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TableButtons {
    private static final Dimension ACTION_BTN_SIZE = new Dimension(62, 24);

    // =====================================================
    // Button cell support (UI only, no backend needed)
    // =====================================================

    // creates the button inside the cell
    static class ActionCell extends JPanel {
        final JButton button = new JButton();

        ActionCell() {
            super(new GridBagLayout());
            button.setPreferredSize(ACTION_BTN_SIZE);
            button.setFocusPainted(false);
            button.setMargin(new Insets(0, 0, 0, 0));
            add(button);
        }

        void configure(JTable table, Object value, boolean selected) {
            // set value from the cell ("update" or "delete") as a button text
            button.setText(value == null ? "" : value.toString());
            // set background when selected or not
            setBackground(selected ? table.getSelectionBackground() : table.getBackground());
        }
    }

    // calls the the button creator, and configure background
    static class ButtonRenderer implements TableCellRenderer {
        // creates the button
        private final ActionCell cell = new ActionCell();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            cell.configure(table, value, isSelected);
            return cell;
        }
    }

    /** The real, clickable JButton that the table swaps in when you click the cell. */
    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final ActionCell cell = new ActionCell();
        private final IntConsumer onClick;
        private int currentRow;

        ButtonEditor(IntConsumer onClick) {
            this.onClick = onClick;
            cell.button.addActionListener(e -> {
                int row = currentRow;
                cancelCellEditing();
                onClick.accept(row);       // row is a VIEW index
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            cell.configure(table, value, isSelected);
            return cell;
        }

        @Override
        public Object getCellEditorValue() {
            return cell.button.getText();
        }
    }
}
