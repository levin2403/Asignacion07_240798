/**
 *
 */
package presentacion;

import dtos.AlumnoTablaDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import negocio.AlumnoNegocio;
import negocio.IAlumnoNegocio;
import negocio.NegocioException;
import persistencia.AlumnoDAO;
import persistencia.ConexionBD;
import persistencia.IAlumnoDAO;
import persistencia.IConexionBD;
import utilerias.JButtonCellEditor;
import utilerias.JButtonRenderer;

/**
 *
 * @author Kevin Jared Sanchez Figueroa 
 * ------- 00000240798
 */
public class FrmCRUD extends javax.swing.JFrame {

    private int pagina = 1; //numero de pagina
    private final int LIMITE = 4; //limite de elementos
    private IAlumnoNegocio alumnoNegocio; //instancia de la clase de alumnos

    /**
     * Aqui hacemos toda la magia.
     *
     * @param alumnoNegocio Objeto de tipo alumno negocio.
     */
    public FrmCRUD(IAlumnoNegocio alumnoNegocio) {
        initComponents();
        //definimos el tamaño del frame
        this.setLocationRelativeTo(this);
        this.setSize(880, 658);

        // seteamos los botones como invisibles 
        btnCancelar.setVisible(false);
        btnRegistrarConfirmar.setVisible(false);

        //definimos metodos iniciales
        this.alumnoNegocio = alumnoNegocio;
        this.ConfiguracionTablaAlumnos();
        this.cargarAlumnosEnTabla();
    }


    /**
     * Configuramos la tabla para que se le inserten los botones de editar y
     * Eliminar, es bien dificil pero se logro.
     * 
     */
    private void ConfiguracionTablaAlumnos() {
        ActionListener onEditarClickListener = new ActionListener() {
            final int columnaId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                //Metodo para editar un alumno
                editar();
            }
        };
        int indiceColumnaEditar = 5;
        TableColumnModel modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaEditar)
                .setCellRenderer(new JButtonRenderer("Editar"));
        modeloColumnas.getColumn(indiceColumnaEditar)
                .setCellEditor(new JButtonCellEditor("Editar",
                        onEditarClickListener));

        ActionListener onEliminarClickListener = new ActionListener() {
            final int columnaId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                //Metodo para eliminar un alumno
                eliminar();
            }
        };
        int indiceColumnaEliminar = 6;
        modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaEliminar)
                .setCellRenderer(new JButtonRenderer("Eliminar"));
        modeloColumnas.getColumn(indiceColumnaEliminar)
                .setCellEditor(new JButtonCellEditor("Eliminar",
                        onEliminarClickListener));
    }

    /**
     * Este metodo nos llenara la tabla de alumnos con todos los elementos.
     * @param alumnosLista 
     */
    private void llenarTablaAlumnos(List<AlumnoTablaDTO> alumnosLista) {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblAlumnos.getModel();

        if (modeloTabla.getRowCount() > 0) {
            for (int i = modeloTabla.getRowCount() - 1; i > -1; i--) {
                modeloTabla.removeRow(i);
            }
        }
        if (alumnosLista != null) {
            alumnosLista.forEach(row -> {
                Object[] fila = new Object[5];
                fila[0] = row.getIdAlumno();
                fila[1] = row.getNombres();
                fila[2] = row.getApellidoPaterno();
                fila[3] = row.getApellidoMaterno();
                fila[4] = row.getEstatus();

                modeloTabla.addRow(fila);
            });
        }
    }
    
    /**
     * Este metodo nos ayuda a datnos la lista paginada.
     * 
     */
    void cargarAlumnosEnTabla() {
        try {
            List<AlumnoTablaDTO> alumnos = alumnoNegocio.alumnosTabla();
            int inicio = (pagina - 1) * LIMITE;
            int fin = Math.min(inicio + LIMITE, alumnos.size());
            List<AlumnoTablaDTO> alumnosPaginados = alumnos.subList(inicio, fin);
            this.llenarTablaAlumnos(alumnosPaginados);  
        } catch (NegocioException ex) {
        }
    }
    
    /**
     * Obtiene el id de la fila seleccionada de la tabla.
     * 
     * @return 
     */
    private int getIdSeleccionado() {
        int indiceFilaSeleccionada = this.tblAlumnos.getSelectedRow();
        if (indiceFilaSeleccionada != -1) {
            DefaultTableModel modelo = (DefaultTableModel) this.tblAlumnos.getModel();
            int indiceColumnaId = 0;
            int idSocioSeleccionado = (int) modelo.getValueAt(indiceFilaSeleccionada, indiceColumnaId);
            return idSocioSeleccionado;
        } else {
            return -1;
        }
    }

    /**
     * Registra un nuevo alumno obteniendo la informacion necesaria de sus 
     * repectivos campos de texto.
     */
    private void registrar() {
        String nombres = txtNombres.getText().trim();
        String apellidoPaterno = txtApellidoPaterno.getText().trim();
        String apellidoMaterno = txtApellidoMaterno.getText().trim();

        try {
            alumnoNegocio.registrar(nombres, apellidoPaterno, apellidoMaterno);
            JOptionPane.showMessageDialog(this, "Registro exitoso", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        cargarAlumnosEnTabla();
    }

    /**
     * Edita a un alumno abriendo un nuevo frame que se encarga de recibir 
     * los nuevos datos.
     * 
     */
    private void editar() {
        // guardamos el numero de fila
        int id = getIdSeleccionado();

        //validamos la columna
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "s?seleccione al menos un alumno a editar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) this.tblAlumnos.getModel();
        int indiceFilaSeleccionada = this.tblAlumnos.getSelectedRow();
        String nombres = (String) modelo.getValueAt(indiceFilaSeleccionada, 1);
        String apellidoPaterno = (String) modelo.getValueAt(indiceFilaSeleccionada, 2);
        String apellidoMaterno = (String) modelo.getValueAt(indiceFilaSeleccionada, 3);

        // abrimos el frame para editar
        FrmEditarAlumno frmEditar = new FrmEditarAlumno(this, id, nombres, apellidoPaterno, apellidoMaterno);
        frmEditar.setVisible(true);
    }

    /**
     * Elimina a el alumno seleccionado de la tabla de alumnos.
     * 
     */
    private void eliminar() {
        // guardamos el numero de fila
        int indiceFilaSeleccionada = this.tblAlumnos.getSelectedRow();

        //validamos la columna
        if (indiceFilaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un alumno para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) this.tblAlumnos.getModel();
        int idAlumno = (int) modelo.getValueAt(indiceFilaSeleccionada, 0);

        // ventana para confirmacion
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este alumno?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        // si la opcion due marcada como positiva eliminamos al alumno 
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                alumnoNegocio.eliminar(idAlumno);
                JOptionPane.showMessageDialog(this, "Alumno eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                cargarAlumnosEnTabla(); //cargamos de vuelta la tabla 
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblNombres = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        lblApellidoPaterno = new javax.swing.JLabel();
        txtApellidoPaterno = new javax.swing.JTextField();
        lblApellidoMaterno = new javax.swing.JLabel();
        txtApellidoMaterno = new javax.swing.JTextField();
        chbActivo = new javax.swing.JCheckBox();
        btnRegistrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        btnAtras = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        lblPagina = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnRegistrarConfirmar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administración de Alumnos");

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTitulo.setText("Administración de Alumnos");

        lblNombres.setText("Nombre(s):");

        lblApellidoPaterno.setText("Apellido Paterno:");

        lblApellidoMaterno.setText("Apellido Materno:");

        chbActivo.setText("Activo");

        btnRegistrar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnRegistrar.setText("Nuevo Registro");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombres", "A. Paterno", "A. Materno", "Estatus", "Editar", "Eliminar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAlumnos);

        btnAtras.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAtras.setText("Atrás");
        btnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtrasActionPerformed(evt);
            }
        });

        btnSiguiente.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });

        lblPagina.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPagina.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPagina.setText("Página 1");
        lblPagina.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnRegistrarConfirmar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnRegistrarConfirmar.setText("Registrar");
        btnRegistrarConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarConfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(btnAtras, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnSiguiente)
                            .addGap(283, 283, 283))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNombres))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtApellidoPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblApellidoPaterno))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblApellidoMaterno)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtApellidoMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(chbActivo)
                                        .addGap(44, 44, 44)
                                        .addComponent(lblTitulo))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCancelar)
                                .addGap(18, 18, 18)
                                .addComponent(btnRegistrarConfirmar)
                                .addGap(18, 18, 18)
                                .addComponent(btnRegistrar)))
                        .addContainerGap(12, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombres)
                    .addComponent(lblApellidoPaterno)
                    .addComponent(lblApellidoMaterno))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidoPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidoMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chbActivo)
                    .addComponent(lblTitulo))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnRegistrarConfirmar)
                    .addComponent(btnRegistrar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAtras)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPagina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSiguiente)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed

        btnCancelar.setVisible(true);
        btnRegistrarConfirmar.setVisible(true);
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed

        if (pagina > 1) {
            pagina--;
            lblPagina.setText("Página " + pagina); // Actualiza el texto del JLabel
            cargarAlumnosEnTabla();
        }
    }//GEN-LAST:event_btnAtrasActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed

        try {
            List<AlumnoTablaDTO> alumnos = alumnoNegocio.alumnosTabla();
            int totalPaginas = (int) Math.ceil((double) alumnos.size() / LIMITE);
            if (pagina < totalPaginas) {
                pagina++;
                cargarAlumnosEnTabla();
                lblPagina.setText("Página " + pagina); // Actualiza el texto del JLabel
            }
        } catch (NegocioException ex) {
            System.out.println(ex.getMessage());
        }
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnRegistrarConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarConfirmarActionPerformed
        // TODO add your handling code here:
        registrar();
    }//GEN-LAST:event_btnRegistrarConfirmarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        
        // Ponemos los campos de texto en blanco
        txtNombres.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");

        // Ocultamos los botones
        btnCancelar.setVisible(false);
        btnRegistrarConfirmar.setVisible(false);
    }//GEN-LAST:event_btnCancelarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // Crea una instancia de ConexionBD (o la implementación real que estés utilizando)
            IConexionBD conexionBD = new ConexionBD();

            // Crea una instancia de AlumnoDAO pasando la conexión como argumento
            IAlumnoDAO alumnoDAO = new AlumnoDAO(conexionBD);

            // Crea una instancia de AlumnoNegocio pasando el DAO como argumento
            IAlumnoNegocio alumnoNegocio = new AlumnoNegocio(alumnoDAO);

            // Crea una instancia de FrmCRUD pasando el negocio como argumento
            FrmCRUD frmCRUD = new FrmCRUD(alumnoNegocio);

            // Hacer visible el JFrame
            frmCRUD.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtras;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegistrarConfirmar;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JCheckBox chbActivo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblApellidoMaterno;
    private javax.swing.JLabel lblApellidoPaterno;
    private javax.swing.JLabel lblNombres;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblAlumnos;
    private javax.swing.JTextField txtApellidoMaterno;
    private javax.swing.JTextField txtApellidoPaterno;
    private javax.swing.JTextField txtNombres;
    // End of variables declaration//GEN-END:variables
}
