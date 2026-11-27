package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;

import ui.TableButtons.ButtonEditor;
import ui.TableButtons.ButtonRenderer;

public class ComputerManagerPanel extends JPanel {

    // =====================================================
    // Constants
    // =====================================================
    private static final int COL_NAME     = 0;
    private static final int COL_CATEGORY = 1;
    private static final int COL_UPDATE   = 2;
    private static final int COL_DELETE   = 3;

    private static final int NOT_EDITING = -1;

    private static final int FORM_WIDTH = 300;
    private static final int ACTION_COL_WIDTH = 80;

    // =====================================================
    // Fields
    // =====================================================
    private DefaultTableModel tableModel;
    private JTable table;

    private JPanel formPanel;
    private TitledBorder formBorder;
    private JTextField nameField;
    private JComboBox<String> categoryComboBox;
    private JButton secondaryButton;   // "Clear" in Add mode, "Cancel" in Edit mode
    private JButton saveButton;

    // Model row currently being edited, or NOT_EDITING (-1) in Add mode.
    // This, not the table selection, decides which mode the form is in.
    // BACKEND TODO: once records have an ID, track the selected computer's ID instead of a row index.
    private int editingRow = NOT_EDITING;

    // BACKEND TODO: add a field `private ComputerManagerController controller;`
    // and receive it through the constructor: ComputerManagerPanel(ComputerManagerController controller).

    // =====================================================
    // Constructor
    // =====================================================
    public ComputerManagerPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 0, 20, 20));

        JLabel title = new JLabel("Computer Manager");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.add(createListPanel(), BorderLayout.CENTER);
        content.add(createFormPanel(), BorderLayout.EAST);
        add(content, BorderLayout.CENTER);

        updateFormMode();

        // BACKEND TODO: load the saved computers here.
        // Ask the controller for all computers and add each one to the table
        // (for example, a refreshTable() method that clears the model and calls addComputer for each).
    }

    // =====================================================
    // LEFT SIDE: computer list
    // =====================================================
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Computers"));

        tableModel = new DefaultTableModel(new String[]{"Name", "Category", "", ""}, 0) {
            // this is to enable editing in the update and delete column
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COL_UPDATE || column == COL_DELETE;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(36);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        TableColumnModel cm = table.getColumnModel();

        fixColumnWidth(cm.getColumn(COL_UPDATE), ACTION_COL_WIDTH);
        fixColumnWidth(cm.getColumn(COL_DELETE), ACTION_COL_WIDTH);

        // changes how the cell looks
        // initially, the cell would only display the word "update" or "delete"
        // ButtonRenderer will make a JButton inside that cell
        // ButtonRenderer returns the updated cell

        cm.getColumn(COL_UPDATE).setCellRenderer(new ButtonRenderer());
        cm.getColumn(COL_UPDATE).setCellEditor(new ButtonEditor(this::startEditing));

        cm.getColumn(COL_DELETE).setCellRenderer(new ButtonRenderer());
        cm.getColumn(COL_DELETE).setCellEditor(new ButtonEditor(this::deleteComputer));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void fixColumnWidth(TableColumn column, int width) {
        column.setMinWidth(width);
        column.setMaxWidth(width);
        column.setPreferredWidth(width);
    }

    // =====================================================
    // RIGHT SIDE: form (fixed width)
    // =====================================================
    private JPanel createFormPanel() {
        formPanel = new JPanel(new BorderLayout());
        formBorder = BorderFactory.createTitledBorder("Add Computer");
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                formBorder, new EmptyBorder(5, 10, 10, 10)));

        formPanel.setPreferredSize(new Dimension(FORM_WIDTH, 0));
        formPanel.setMinimumSize(new Dimension(FORM_WIDTH, 0));

        nameField = new JTextField();
        categoryComboBox = new JComboBox<>(new String[]{"Gaming", "Standard"});
        // BACKEND TODO (optional): load the category list from a shared source (e.g., a Category
        // enum or the service) so it isn't hardcoded here.

        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addField(fields, gbc, "Computer Name", nameField);
        addField(fields, gbc, "Category", categoryComboBox);

        // Same action in both modes: reset the form back to Add mode.
        // Only the label changes (Clear / Cancel).
        secondaryButton = new JButton("Clear");
        secondaryButton.addActionListener(e -> resetForm());   // UI only, no backend needed

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveComputer());     // backend hook is inside saveComputer()

        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 0));
        buttons.add(secondaryButton);
        buttons.add(saveButton);

        gbc.insets = new Insets(15, 0, 0, 0);
        fields.add(buttons, gbc);

        formPanel.add(fields, BorderLayout.NORTH);
        return formPanel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.insets = new Insets(8, 0, 2, 0);
        panel.add(new JLabel(label), gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(field, gbc);
    }

    // =====================================================
    // CRUD logic
    // BACKEND TODO: this whole section currently works on the table model only.
    // Move the decisions into controller/service; keep here only what the screen shows.
    // =====================================================

    /** Adds one row to the table (display only). */
    private void addComputer(String name, String category) {
        // BACKEND TODO: this should only display a row. The controller should save the
        // computer first, then the UI adds the row (or reloads the whole list).
        tableModel.addRow(new Object[]{name, category, "Update", "Delete"});
    }

    /** Called by the row's Update button (viewRow is a VIEW index). */
    private void startEditing(int viewRow) {
        editingRow = table.convertRowIndexToModel(viewRow);

        // BACKEND TODO (optional): instead of reading the table, ask the controller for the
        // Computer by ID so the form always shows the stored data.
        nameField.setText((String) tableModel.getValueAt(editingRow, COL_NAME));
        categoryComboBox.setSelectedItem(tableModel.getValueAt(editingRow, COL_CATEGORY));

        updateFormMode();
        nameField.requestFocusInWindow();
        nameField.selectAll();
    }

    private void saveComputer() {
        String name = nameField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();

        // BACKEND TODO: validation (empty name, duplicate name) belongs in ComputerService.
        // Here, call the controller and show the error message it returns, if any.
        // Keep these checks until the service can do them.
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Computer name is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (isDuplicateName(name, editingRow)) {
            JOptionPane.showMessageDialog(this, "A computer named \"" + name + "\" already exists.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (editingRow != NOT_EDITING) {
            // BACKEND TODO: call controller.updateComputer(id, name, category), then refresh the row.
            tableModel.setValueAt(name, editingRow, COL_NAME);
            tableModel.setValueAt(category, editingRow, COL_CATEGORY);
        } else {
            // BACKEND TODO: call controller.addComputer(name, category), then show the new row.
            addComputer(name, category);
        }

        resetForm();   // both paths return to Add mode
    }

    private void deleteComputer(int viewRow) {
        int modelRow = table.convertRowIndexToModel(viewRow);
        String name = (String) tableModel.getValueAt(modelRow, COL_NAME);

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete " + name + "?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) return;

        // BACKEND TODO: call controller.deleteComputer(id) BEFORE removing the row.
        // The service should refuse if the computer has an active session, and the UI
        // should then show that message and NOT remove the row.
        tableModel.removeRow(modelRow);

        if (editingRow == modelRow) {
            resetForm();                 // the record being edited is gone
        } else if (editingRow > modelRow) {
            editingRow--;                // rows shifted up; keep pointing at the same record
        }
        // otherwise: leave the in-progress edit alone
    }

    private boolean isDuplicateName(String name, int ignoreModelRow) {
        // BACKEND TODO: move this check to ComputerService (it should check the stored list,
        // not the table), then delete this method.
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (i != ignoreModelRow
                    && name.equalsIgnoreCase((String) tableModel.getValueAt(i, COL_NAME))) {
                return true;
            }
        }
        return false;
    }

    // =====================================================
    // Form helpers
    // =====================================================

    /** Back to Add mode with empty fields. Used by Clear, Cancel, and after Save. */
    private void resetForm() {
        editingRow = NOT_EDITING;
        table.clearSelection();
        nameField.setText("");
        categoryComboBox.setSelectedIndex(0);
        updateFormMode();
        nameField.requestFocusInWindow();
    }

    /** Syncs the title and the left button's label with the current mode. */
    private void updateFormMode() {
        boolean editing = editingRow != NOT_EDITING;

        if (editing) {
            String name = (String) tableModel.getValueAt(editingRow, COL_NAME);
            formBorder.setTitle("Edit Computer: " + name);
        } else {
            formBorder.setTitle("Add Computer");
        }

        secondaryButton.setText(editing ? "Cancel" : "Clear");
        formPanel.repaint();
    }
}