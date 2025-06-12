import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KasirFrame frame = new KasirFrame();
            frame.setVisible(true);
        });
    }

    // Inner class untuk representasi Menu
    static class Menu {
        private final String nama;
        private final double harga;

        public Menu(String nama, double harga) {
            this.nama = nama;
            this.harga = harga;
        }

        public String getNama() {
            return nama;
        }

        public double getHarga() {
            return harga;
        }
    }

    // Inner class untuk menangani logika transaksi
    static class Transaksi {
        private Menu menuMakanan;
        private int jumlahMakanan;
        private final List<Menu> minuman;
        private final List<Integer> jumlahMinuman;
        private String jenisLayanan; // "delivery" atau "take away"
        private double totalBayar;

        public Transaksi() {
            minuman = new ArrayList<>();
            jumlahMinuman = new ArrayList<>();
            totalBayar = 0;
        }

        public void setMenuMakanan(Menu menu, int jumlah) {
            this.menuMakanan = menu;
            this.jumlahMakanan = jumlah;
            hitungTotal();
        }

        public void addMinuman(Menu minuman, int jumlah) {
            this.minuman.add(minuman);
            this.jumlahMinuman.add(jumlah);
            hitungTotal();
        }

        public void setJenisLayanan(String jenisLayanan) {
            this.jenisLayanan = jenisLayanan;
            hitungTotal();
        }

        private void hitungTotal() {
            totalBayar = 0;

            // Hitung total makanan
            if (menuMakanan != null) {
                totalBayar += menuMakanan.getHarga() * jumlahMakanan;
            }

            // Hitung total minuman
            for (int i = 0; i < minuman.size(); i++) {
                totalBayar += minuman.get(i).getHarga() * jumlahMinuman.get(i);
            }

            // Hitung biaya layanan
            if ("delivery".equals(jenisLayanan)) {
                totalBayar += totalBayar * 0.1; // Tambah 10% untuk delivery
            }
        }

        public double getTotalBayar() {
            return totalBayar;
        }

        public void reset() {
            menuMakanan = null;
            jumlahMakanan = 0;
            minuman.clear();
            jumlahMinuman.clear();
            jenisLayanan = null;
            totalBayar = 0;
        }
    }

    // Inner class untuk GUI utama
    static class KasirFrame extends JFrame {
        private final Transaksi transaksi;
        private final Menu[] menuMakanan;
        private final Menu[] menuMinuman;

        // Komponen GUI
        private final JComboBox<String> cbPaketMakanan;
        private final JTextField tfNamaMenu;
        private final JTextField tfHargaMenu;
        private final JTextField tfJumlah;
        private final JTextField tfTotalHarga;
        private final JCheckBox[] cbMinuman;
        private final JTextField[] tfJumlahMinuman;
        private final JRadioButton rbDelivery;
        private final JRadioButton rbTakeAway;
        private final JTextField tfBiayaLayanan;
        private final JTextField tfTotalBayar;
        private final JTextField tfUangBayar;
        private final JTextField tfKembalian;

        public KasirFrame() {
            // Inisialisasi data menu
            menuMakanan = new Menu[]{
                    new Menu("Paket Sushi Combo", 50000),
                    new Menu("Paket Ramen Special", 45000),
                    new Menu("Paket Tempura Set", 55000)
            };

            menuMinuman = new Menu[]{
                    new Menu("Air Mineral", 5000),
                    new Menu("Juice", 10000),
                    new Menu("Es Teh Manis", 8000)
            };

            transaksi = new Transaksi();

            // Setup Frame
            setTitle("Kasir Oishi ne");
            setSize(850, 650);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null); // Center window
            getContentPane().setBackground(new Color(245, 245, 245));

            // Main panel dengan layout grid
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            mainPanel.setBackground(new Color(245, 245, 245));

            // Panel untuk input pesanan
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
            inputPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Input Pesanan"),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            inputPanel.setBackground(Color.WHITE);
            inputPanel.setPreferredSize(new Dimension(600, 450));

            // Panel untuk makanan
            JPanel makananPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            makananPanel.setBorder(BorderFactory.createTitledBorder("Paket Makanan"));
            makananPanel.setBackground(Color.WHITE);

            // Komponen untuk makanan
            makananPanel.add(new JLabel("Paket Makanan:"));
            cbPaketMakanan = new JComboBox<>();
            cbPaketMakanan.addItem(""); // Item kosong di awal
            for (Menu menu : menuMakanan) {
                cbPaketMakanan.addItem(menu.getNama());
            }
            makananPanel.add(cbPaketMakanan);

            makananPanel.add(new JLabel("Nama Menu:"));
            tfNamaMenu = new JTextField();
            tfNamaMenu.setEditable(false);
            makananPanel.add(tfNamaMenu);

            makananPanel.add(new JLabel("Harga:"));
            tfHargaMenu = new JTextField();
            tfHargaMenu.setEditable(false);
            makananPanel.add(tfHargaMenu);

            makananPanel.add(new JLabel("Jumlah:"));
            tfJumlah = new JTextField();
            makananPanel.add(tfJumlah);

            makananPanel.add(new JLabel("Total Harga:"));
            tfTotalHarga = new JTextField();
            tfTotalHarga.setEditable(false);
            makananPanel.add(tfTotalHarga);

            // Panel untuk minuman
            JPanel minumanPanel = new JPanel();
            minumanPanel.setLayout(new BoxLayout(minumanPanel, BoxLayout.Y_AXIS));
            minumanPanel.setBorder(BorderFactory.createTitledBorder("Minuman"));
            minumanPanel.setBackground(Color.WHITE);

            cbMinuman = new JCheckBox[menuMinuman.length];
            tfJumlahMinuman = new JTextField[menuMinuman.length];

            for (int i = 0; i < menuMinuman.length; i++) {
                JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                rowPanel.setBackground(Color.WHITE);
                cbMinuman[i] = new JCheckBox(menuMinuman[i].getNama() + " (Rp" + (int)menuMinuman[i].getHarga() + ")");
                tfJumlahMinuman[i] = new JTextField(5);
                tfJumlahMinuman[i].setPreferredSize(new Dimension(50, 25));

                rowPanel.add(cbMinuman[i]);
                rowPanel.add(new JLabel("Jumlah:"));
                rowPanel.add(tfJumlahMinuman[i]);
                minumanPanel.add(rowPanel);
            }

            // Panel untuk layanan
            JPanel layananPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            layananPanel.setBorder(BorderFactory.createTitledBorder("Jenis Layanan"));
            layananPanel.setBackground(Color.WHITE);

            ButtonGroup bgLayanan = new ButtonGroup();
            rbDelivery = new JRadioButton("Delivery (+10%)");
            rbTakeAway = new JRadioButton("Take Away");
            bgLayanan.add(rbDelivery);
            bgLayanan.add(rbTakeAway);

            tfBiayaLayanan = new JTextField(10);
            tfBiayaLayanan.setEditable(false);

            layananPanel.add(rbDelivery);
            layananPanel.add(rbTakeAway);
            layananPanel.add(new JLabel("Biaya Layanan:"));
            layananPanel.add(tfBiayaLayanan);

            // Gabungkan panel input
            inputPanel.add(makananPanel);
            inputPanel.add(Box.createVerticalStrut(15));
            inputPanel.add(minumanPanel);
            inputPanel.add(Box.createVerticalStrut(15));
            inputPanel.add(layananPanel);

            // Panel untuk pembayaran
            JPanel paymentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            paymentPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Pembayaran"),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            paymentPanel.setBackground(Color.WHITE);

            tfTotalBayar = new JTextField();
            tfTotalBayar.setEditable(false);
            tfUangBayar = new JTextField();
            tfKembalian = new JTextField();
            tfKembalian.setEditable(false);

            paymentPanel.add(new JLabel("Total Bayar:"));
            paymentPanel.add(tfTotalBayar);
            paymentPanel.add(new JLabel("Uang Bayar:"));
            paymentPanel.add(tfUangBayar);
            paymentPanel.add(new JLabel("Kembalian:"));
            paymentPanel.add(tfKembalian);

            // Panel untuk tombol
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(new Color(245, 245, 245));

            JButton btnBersih = new JButton("Bersih");
            btnBersih.setBackground(new Color(70, 130, 180));
            btnBersih.setForeground(Color.WHITE);
            btnBersih.setFocusPainted(false);

            JButton btnProses = new JButton("Proses");
            btnProses.setBackground(new Color(60, 179, 113));
            btnProses.setForeground(Color.WHITE);
            btnProses.setFocusPainted(false);

            JButton btnKeluar = new JButton("Keluar");
            btnKeluar.setBackground(new Color(205, 92, 92));
            btnKeluar.setForeground(Color.WHITE);
            btnKeluar.setFocusPainted(false);

            // Event handlers
            cbPaketMakanan.addActionListener(this::pilihMakananAction);

            tfJumlah.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        hitungTotalHarga();
                    }
                }
            });

            rbDelivery.addActionListener(this::layananAction);
            rbTakeAway.addActionListener(this::layananAction);

            btnBersih.addActionListener(this::bersihkanFormAction);
            btnProses.addActionListener(this::prosesTransaksiAction);
            btnKeluar.addActionListener(this::keluarAplikasiAction);

            buttonPanel.add(btnBersih);
            buttonPanel.add(btnProses);
            buttonPanel.add(btnKeluar);

            // Gabungkan semua panel
            mainPanel.add(inputPanel, BorderLayout.CENTER);
            mainPanel.add(paymentPanel, BorderLayout.SOUTH);

            add(mainPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            // Inisialisasi form - tidak pilih paket makanan di awal
            cbPaketMakanan.setSelectedIndex(0); // Pilih item kosong
            bersihkanForm();
        }

        private void pilihMakananAction(ActionEvent e) {
            pilihMakanan();
        }

        private void layananAction(ActionEvent e) {
            setLayanan();
        }

        private void bersihkanFormAction(ActionEvent e) {
            bersihkanForm();
        }

        private void prosesTransaksiAction(ActionEvent e) {
            prosesTransaksi();
        }

        private void keluarAplikasiAction(ActionEvent e) {
            keluarAplikasi();
        }

        private void pilihMakanan() {
            int index = cbPaketMakanan.getSelectedIndex();
            if (index > 0) { // Index 0 adalah item kosong
                Menu menu = menuMakanan[index - 1]; // -1 karena index 0 adalah item kosong
                tfNamaMenu.setText(menu.getNama());
                tfHargaMenu.setText(String.valueOf((int)menu.getHarga()));
            } else {
                tfNamaMenu.setText("");
                tfHargaMenu.setText("");
                tfTotalHarga.setText("");
            }
        }

        private void hitungTotalHarga() {
            int index = cbPaketMakanan.getSelectedIndex();
            if (index > 0) { // Hanya hitung jika ada makanan yang dipilih
                try {
                    int jumlah = Integer.parseInt(tfJumlah.getText());
                    Menu menu = menuMakanan[index - 1]; // -1 karena index 0 adalah item kosong
                    tfTotalHarga.setText(String.valueOf((int)(menu.getHarga() * jumlah)));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Masukkan jumlah yang valid", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                tfTotalHarga.setText("0");
            }
            updateTotalBayar();
        }

        private void setLayanan() {
            if (rbDelivery.isSelected()) {
                transaksi.setJenisLayanan("delivery");
                tfBiayaLayanan.setText("10%");
            } else if (rbTakeAway.isSelected()) {
                transaksi.setJenisLayanan("take away");
                tfBiayaLayanan.setText("0");
            }
            updateTotalBayar();
        }

        private void updateTotalBayar() {
            // Reset transaksi
            transaksi.reset();

            // Tambahkan makanan jika ada yang dipilih
            int index = cbPaketMakanan.getSelectedIndex();
            if (index > 0) {
                try {
                    int jumlah = Integer.parseInt(tfJumlah.getText());
                    Menu menu = menuMakanan[index - 1]; // -1 karena index 0 adalah item kosong
                    transaksi.setMenuMakanan(menu, jumlah);
                } catch (NumberFormatException e) {
                    // Jumlah tidak valid, skip
                }
            }

            // Tambahkan minuman
            for (int i = 0; i < cbMinuman.length; i++) {
                if (cbMinuman[i].isSelected()) {
                    try {
                        int jumlah = Integer.parseInt(tfJumlahMinuman[i].getText());
                        transaksi.addMinuman(menuMinuman[i], jumlah);
                    } catch (NumberFormatException e) {
                        // Jumlah tidak valid, skip
                    }
                }
            }

            // Tambahkan layanan
            if (rbDelivery.isSelected()) {
                transaksi.setJenisLayanan("delivery");
            } else if (rbTakeAway.isSelected()) {
                transaksi.setJenisLayanan("take away");
            }

            // Update tampilan
            tfTotalBayar.setText(String.valueOf((int)transaksi.getTotalBayar()));
        }

        private void prosesTransaksi() {
            try {
                double uangBayar = Double.parseDouble(tfUangBayar.getText());
                double totalBayar = transaksi.getTotalBayar();

                if (uangBayar >= totalBayar) {
                    double kembalian = uangBayar - totalBayar;
                    tfKembalian.setText(String.valueOf((int)kembalian));
                } else {
                    JOptionPane.showMessageDialog(this, "Uang bayar kurang!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Masukkan uang bayar yang valid", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void bersihkanForm() {
            transaksi.reset();
            cbPaketMakanan.setSelectedIndex(0); // Pilih item kosong
            tfNamaMenu.setText("");
            tfHargaMenu.setText("");
            tfJumlah.setText("");
            tfTotalHarga.setText("");

            for (int i = 0; i < cbMinuman.length; i++) {
                cbMinuman[i].setSelected(false);
                tfJumlahMinuman[i].setText("");
            }

            rbDelivery.setSelected(false);
            rbTakeAway.setSelected(false);
            tfBiayaLayanan.setText("");
            tfTotalBayar.setText("");
            tfUangBayar.setText("");
            tfKembalian.setText("");
        }

        private void keluarAplikasi() {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah anda yakin ingin menutup aplikasi?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
}