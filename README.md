# 📚 Projek JDBC & Aplikasi Presensi Mahasiswa

Repositori ini berisi dua proyek utama sebagai bagian dari tugas akhir semester:

1. **ProgramJDBC** – Aplikasi konsol Java untuk operasi CRUD ke database Oracle.
2. **PresensiMahasiswaGUI** – Aplikasi GUI presensi mahasiswa dengan integrasi database Oracle.

Kedua proyek menggunakan **Java Database Connectivity (JDBC)** untuk berinteraksi dengan **Oracle Database 11g XE**, menunjukkan kemampuan dalam pengembangan aplikasi berbasis data.

---

## 🛠 Teknologi yang Digunakan

- **Bahasa Pemrograman**: Java 17
- **Database**: Oracle Database 11g Express Edition (XE)
- **Driver JDBC**: `ojdbc17.jar`
- **Tools**: SQL Developer, IntelliJ IDEA
- **GUI Framework**: Swing (untuk aplikasi presensi)

---

## 🔧 Proyek 1: ProgramJDBC

### Deskripsi
Aplikasi konsol sederhana yang mengimplementasikan operasi CRUD (Create, Read, Update, Delete) terhadap tabel `MAHASISWA` di database Oracle. Program menyediakan menu interaktif untuk memudahkan pengguna melakukan manipulasi data.

### Fitur Utama
- Koneksi ke Oracle Database menggunakan JDBC.
- Menampilkan data mahasiswa.
- Menambahkan data baru.
- Menghapus data berdasarkan NIM.
- Memperbarui nama dan IPK.


## 🖥️ Proyek 2: PresensiMahasiswaGUI
### Deskripsi:
Aplikasi desktop berbasis GUI untuk mengelola presensi mahasiswa. Pengguna dapat mendaftar, login, dan mengisi status kehadiran sesuai mata kuliah. Semua data disimpan secara persisten di database Oracle.

### Fitur Utama: 
Form pendaftaran dan login mahasiswa.
Pemilihan status kehadiran: Hadir, Izin, Alpha, Tidak Hadir.
Validasi otomatis:
Cek jatah alpha (maksimal 3 kali).
Update pertemuan otomatis.
Cegah duplikasi presensi harian.
Riwayat presensi ditampilkan dalam tabel dinamis.
Mode "lihat saja" untuk review data tanpa mengubah.
Integrasi Database
Seluruh penyimpanan file teks (*.txt) telah diganti menjadi database Oracle untuk meningkatkan keandalan dan skalabilitas.



