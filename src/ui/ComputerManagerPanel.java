package ui;

import service.ComputerService;
import model.Computer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import ui.TableButtons.ButtonEditor;
import ui.TableButtons.ButtonRenderer;

public class ComputerManagerPanel extends JPanel {
    // constants
    private static final int COL_NAME     = 0;
    private static final int COL_CATEGORY = 1;
    private static final int COL_UPDATE   = 2;
    private static final int COL_DELETE   = 3;

    private static final int FORM_WIDTH = 300;
    private static final int ACTION_COL_WIDTH = 80;

    private final ComputerService service;

    // components
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel formPanel;
    private TitledBorder formBorder;
    private JTextField nameField;
    private JComboBox<String> categoryComboBox;
    private JButton secondaryButton;
    private JButton saveButton;

    // UI computers state
    private List<Computer> computers = new ArrayList<>();
    private Computer editingComputer = null;

    public ComputerManagerPanel(ComputerService service) {
        this.service = service;

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
        refreshTable();
    }

    // computer list (left side)
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

    // helper
    private void fixColumnWidth(TableColumn column, int width) {
        column.setMinWidth(width);
        column.setMaxWidth(width);
        column.setPreferredWidth(width);
    }

    // form (right side)
    private JPanel createFormPanel() {
        formPanel = new JPanel(new BorderLayout());
        formBorder = BorderFactory.createTitledBorder("Add Computer");
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            formBorder, new EmptyBorder(5, 10, 10, 10)));
        formPanel.setPreferredSize(new Dimension(FORM_WIDTH, 0));
        formPanel.setMinimumSize(new Dimension(FORM_WIDTH, 0));

        nameField = new JTextField();
        categoryComboBox = new JComboBox<>(new String[]{"Standard", "Gaming", "Premium"});

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
        secondaryButton.addActionListener(e -> resetForm());
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveComputer());

        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 0));
        buttons.add(secondaryButton);
        buttons.add(saveButton);
        gbc.insets = new Insets(15, 0, 0, 0);
        fields.add(buttons, gbc);

        formPanel.add(fields, BorderLayout.NORTH);
        return formPanel;
    }

    // helper
    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.insets = new Insets(8, 0, 2, 0);
        panel.add(new JLabel(label), gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(field, gbc);
    }

    // reloads the table from the controller. Call after any change to the data.
    private void refreshTable() {
        computers = service.getAllComputers();
        tableModel.setRowCount(0);
        for (Computer c : computers) {
            tableModel.addRow(new String[]{c.getName(), c.getCategory(), "Update", "Delete"});
        }
    }

    // called by update button
    private void startEditing(int viewRow) {
        // gets the current row being edited
        editingComputer = computers.get(table.convertRowIndexToModel(viewRow));

        nameField.setText(editingComputer.getName());
        categoryComboBox.setSelectedItem(editingComputer.getCategory());

        updateFormMode();
        nameField.requestFocusInWindow();
        nameField.selectAll();
    }

    private void saveComputer() {
        String name = nameField.getText();
        String category = (String) categoryComboBox.getSelectedItem();

        try {
            if (editingComputer != null) {
                service.updateComputer(editingComputer.getId(), name, category);
            } else {
                service.addComputer(name, category);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        refreshTable();
        resetForm();
    }

    private void deleteComputer(int viewRow) {
        // gets the current row being deleted
        Computer computer = computers.get(table.convertRowIndexToModel(viewRow));

        String message = "Delete " + computer.getName() + "?";
        int choice = JOptionPane.showConfirmDialog(this, message, "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) return;

        try {
            service.deleteComputer(computer.getId());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Cannot Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // only resets form when deleting the computer that is currently being edited
        if (editingComputer != null && editingComputer.getId() == computer.getId()) {
            resetForm();
        }
        refreshTable();
    }

    // back to add mode with empty fields
    private void resetForm() {
        editingComputer = null;
        table.clearSelection();
        nameField.setText("");
        categoryComboBox.setSelectedIndex(0);
        updateFormMode();
        nameField.requestFocusInWindow();
    }

    // syncs the title and the left button's label with the current mode
    private void updateFormMode() {
        boolean editing = editingComputer != null;

        if (editing) {
            formBorder.setTitle("Edit Computer: " + editingComputer.getName());
        } else {
            formBorder.setTitle("Add Computer");
        }

        secondaryButton.setText(editing ? "Cancel" : "Clear");
        formPanel.repaint();
    }
}