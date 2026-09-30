# Hướng dẫn cài đặt môi trường Backend (Java + Spring Boot + MySQL) — Step by step

> Dành cho người mới bắt đầu, máy **Windows 10/11**.
> Làm **lần lượt từ trên xuống**, xong bước nào thì tick ✅ bước đó. Mỗi phần đều có mục **"Kiểm tra"**. Nếu kết quả kiểm tra không giống hướng dẫn thì **dừng lại, xem mục "Lỗi thường gặp"** ở cuối phần đó, đừng làm tiếp.

---

## Mục lục

- [Phần 0. Chuẩn bị](#phần-0-chuẩn-bị)
- [Phần 1. Cài Java (JDK 17)](#phần-1-cài-java-jdk-17)
- [Phần 2. Cài IntelliJ IDEA](#phần-2-cài-intellij-idea)
- [Phần 3. Tạo project Spring Boot và chạy "Hello World"](#phần-3-tạo-project-spring-boot-và-chạy-hello-world)
- [Phần 4. Cài MySQL Server + MySQL Workbench](#phần-4-cài-mysql-server--mysql-workbench)
- [Phần 5. Tạo database (schema) và user cho project](#phần-5-tạo-database-schema-và-user-cho-project)
- [Phần 6. Kết nối Spring Boot với MySQL](#phần-6-kết-nối-spring-boot-với-mysql)
- [Phần 7. Commit lên Git](#phần-7-commit-lên-git)
- [Checklist cuối cùng](#checklist-cuối-cùng)

---

## Phần 0. Chuẩn bị

### 0.1. Những gì sẽ cài

| Công cụ | Phiên bản | Dùng để làm gì |
|---|---|---|
| JDK (Java Development Kit) | **17** (bản Eclipse Temurin) | Biên dịch và chạy code Java |
| IntelliJ IDEA | Bản mới nhất | IDE để viết code Java/Spring Boot |
| Spring Boot | Bản ổn định mới nhất trên start.spring.io | Framework làm Backend REST API |
| Maven | **Không cần cài riêng** — dùng Maven Wrapper (`mvnw`) có sẵn trong project | Quản lý thư viện, build project |
| MySQL Server | **8.4 LTS** (hoặc 8.0+) | Cơ sở dữ liệu |
| MySQL Workbench | Bản mới nhất | Giao diện đồ họa để thao tác với MySQL |

### 0.2. Một số khái niệm cần biết trước

- **JDK**: bộ công cụ để lập trình Java (gồm `java` để chạy và `javac` để biên dịch).
- **Biến môi trường (Environment Variable)**: các "biến" mà Windows lưu lại để chương trình khác đọc. Ví dụ `JAVA_HOME` cho các công cụ biết JDK nằm ở đâu.
- **PATH**: một biến môi trường đặc biệt, chứa danh sách thư mục. Khi gõ lệnh `java` trong terminal, Windows sẽ tìm file `java.exe` trong các thư mục có trong PATH.
- **Schema / Database**: trong MySQL, "schema" và "database" là **một**. Project này dùng database tên `mini_ecommerce`.
- **Terminal**: cửa sổ gõ lệnh. Trên Windows dùng **PowerShell** (bấm phím `Windows`, gõ `PowerShell`, Enter) hoặc Terminal có sẵn trong IntelliJ.

### 0.3. Đã có source code của project chưa?

Em cần clone repository về máy trước (nếu chưa làm):

```powershell
git clone https://github.com/MinhQuan2k3/mini-e-commerce.git
cd mini-e-commerce
```

Trong thư mục project sẽ có sẵn các thư mục: `backend/`, `frontend/`, `database/`, `docs/`. Code Spring Boot sẽ đặt trong **`backend/`**.

> 💡 **Mẹo**: Nên để project ở đường dẫn **không có dấu tiếng Việt và không có dấu cách**, ví dụ `D:\projects\mini-e-commerce`. Một số công cụ Java/Maven thỉnh thoảng lỗi với đường dẫn có ký tự đặc biệt.

---

## Phần 1. Cài Java (JDK 17)

### Bước 1.1. Kiểm tra máy đã có Java chưa

Mở **PowerShell**, gõ:

```powershell
java -version
```

- Nếu hiện `'java' is not recognized...` → máy chưa có Java, làm tiếp **Bước 1.2**.
- Nếu hiện `openjdk version "17.x.x"` → đã có JDK 17, có thể nhảy tới **Bước 1.5** để kiểm tra `JAVA_HOME`.
- Nếu hiện phiên bản khác (ví dụ `1.8`, `11`, `21`) → vẫn cài thêm JDK 17 theo hướng dẫn dưới, sau đó sửa `JAVA_HOME` và `PATH` trỏ về JDK 17.

### Bước 1.2. Tải JDK 17

1. Vào trang: **https://adoptium.net/temurin/releases/?version=17**
2. Chọn các bộ lọc:
   - **Operating System**: `Windows`
   - **Architecture**: `x64`
   - **Package Type**: `JDK`
   - **Version**: `17 - LTS`
3. Tải file có đuôi **`.msi`** (ví dụ `OpenJDK17U-jdk_x64_windows_hotspot_17.0.xx.msi`).

> ❓ **Tại sao Temurin?** Đây là bản OpenJDK miễn phí, phổ biến, dùng tốt cho học tập và làm việc. Tài liệu `environment-setup.md` ghi Java `17.0.2` — dùng bản 17 mới nhất (17.0.x) là được, cùng major version 17.

### Bước 1.3. Cài đặt JDK

1. Chạy file `.msi` vừa tải → **Next**.
2. Ở màn hình **Custom Setup**, sẽ thấy danh sách tính năng. Để ý 2 mục:
   - `Set JAVA_HOME variable`
   - `Add to PATH`
3. Để **tự tay học cách cấu hình**, em **để mặc định (không bật `Set JAVA_HOME variable`)** rồi làm thủ công ở Bước 1.4.
   *(Nếu muốn nhanh: bấm vào mục `Set JAVA_HOME variable` → chọn `Will be installed on local hard drive`, khi đó có thể bỏ qua Bước 1.4 nhưng vẫn phải làm Bước 1.5 để kiểm tra.)*
4. Ghi nhớ **đường dẫn cài đặt** hiển thị ở màn hình này, thường là:
   ```text
   C:\Program Files\Eclipse Adoptium\jdk-17.0.xx.x-hotspot\
   ```
5. **Next** → **Install** → **Finish**.

### Bước 1.4. Thiết lập biến môi trường `JAVA_HOME` và `PATH` (thủ công)

**a) Lấy đúng đường dẫn JDK**

1. Mở **File Explorer**, đi tới `C:\Program Files\Eclipse Adoptium\`.
2. Mở thư mục `jdk-17.0.xx...-hotspot`. Bên trong phải thấy các thư mục `bin`, `lib`, `conf`...
3. Click vào **thanh địa chỉ** của File Explorer → copy đường dẫn. Ví dụ:
   ```text
   C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot
   ```
   ⚠️ Đường dẫn này **không có** `\bin` ở cuối.

**b) Mở cửa sổ Environment Variables**

1. Bấm phím `Windows`, gõ **`environment`**.
2. Chọn **"Edit the system environment variables"** (Chỉnh sửa biến môi trường hệ thống).
3. Cửa sổ **System Properties** hiện ra → bấm nút **Environment Variables...** ở góc dưới.

Cửa sổ có 2 khung:
- **User variables for <tên user>** (phía trên): chỉ áp dụng cho tài khoản hiện tại.
- **System variables** (phía dưới): áp dụng cho toàn máy (cần quyền Admin).

Em dùng khung **System variables** (nếu không có quyền Admin thì dùng khung User variables, cách làm giống hệt).

**c) Tạo biến `JAVA_HOME`**

1. Trong khung **System variables** → bấm **New...**
2. Điền:
   - **Variable name**: `JAVA_HOME`
   - **Variable value**: dán đường dẫn đã copy ở bước a), ví dụ `C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot`
3. Bấm **OK**.

**d) Thêm Java vào `PATH`**

1. Trong khung **System variables**, tìm dòng tên **`Path`** → chọn → bấm **Edit...**
2. Bấm **New** → gõ:
   ```text
   %JAVA_HOME%\bin
   ```
3. Chọn dòng vừa thêm → bấm **Move Up** nhiều lần để đưa lên **trên cùng** (tránh bị Java cũ khác trong máy "chiếm chỗ").
4. Nếu trong danh sách có dòng kiểu `C:\Program Files\Common Files\Oracle\Java\javapath` hoặc đường dẫn Java cũ khác → xóa đi hoặc đưa xuống dưới `%JAVA_HOME%\bin`.
5. Bấm **OK** → **OK** → **OK** để đóng hết các cửa sổ.

### Bước 1.5. Kiểm tra

⚠️ **Đóng hết các cửa sổ PowerShell/Terminal cũ và mở cửa sổ mới** (terminal cũ không nhận biến môi trường mới).

Gõ lần lượt:

```powershell
java -version
```

Kết quả mong đợi (số chi tiết có thể khác):

```text
openjdk version "17.0.13" 2024-10-15
OpenJDK Runtime Environment Temurin-17.0.13+11 (build 17.0.13+11)
OpenJDK 64-Bit Server VM Temurin-17.0.13+11 (build 17.0.13+11, mixed mode, sharing)
```

```powershell
javac -version
```

Kết quả mong đợi:

```text
javac 17.0.13
```

```powershell
echo $env:JAVA_HOME
```

Kết quả mong đợi: in ra đúng đường dẫn JDK đã đặt, ví dụ `C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot`.

> 💡 Nếu dùng **Command Prompt (cmd)** thay vì PowerShell thì lệnh là `echo %JAVA_HOME%`.

✅ Cả 3 lệnh đều ra kết quả đúng → xong Phần 1.

### Lỗi thường gặp — Phần 1

| Hiện tượng | Cách xử lý |
|---|---|
| `'java' is not recognized as an internal or external command` | Chưa thêm `%JAVA_HOME%\bin` vào `Path`, hoặc chưa mở lại terminal mới. |
| `java -version` ra phiên bản khác 17 | Có Java khác đứng trước trong `Path`. Đưa `%JAVA_HOME%\bin` lên trên cùng; xóa dòng `...\Oracle\Java\javapath` nếu có. |
| `echo $env:JAVA_HOME` in ra trống | Chưa tạo biến `JAVA_HOME` hoặc chưa mở terminal mới. |
| `JAVA_HOME` trỏ tới `...\bin` | Sai. `JAVA_HOME` phải là thư mục **cha** của `bin`. |
| Đã làm đúng mà vẫn không nhận | Khởi động lại máy rồi thử lại. |

---

## Phần 2. Cài IntelliJ IDEA

### Bước 2.1. Tải IntelliJ IDEA

1. Vào: **https://www.jetbrains.com/idea/download/?section=windows**
2. Tải bản **IntelliJ IDEA** cho Windows (file `.exe`).

> 💡 IntelliJ có các tính năng cơ bản dùng **miễn phí**, đủ để làm project này. Các tính năng hỗ trợ Spring nâng cao cần bản trả phí (Ultimate). Nếu là sinh viên, có thể đăng ký license miễn phí tại https://www.jetbrains.com/community/education/ — **không bắt buộc**.
> Hướng dẫn này **không phụ thuộc** vào bản trả phí: project sẽ được tạo trên web start.spring.io (Phần 3).

### Bước 2.2. Cài đặt

1. Chạy file `.exe` → **Next**.
2. Chọn thư mục cài (để mặc định) → **Next**.
3. Màn hình **Installation Options**, nên tick:
   - ☑ **Create Desktop Shortcut** → IntelliJ IDEA
   - ☑ **Add "Open Folder as Project"** (thêm menu chuột phải "Open Folder as IntelliJ Project")
   - ☑ **Create Associations** → `.java`
   - ☑ **Update PATH Variable** (có thể cần restart máy)
4. **Next** → **Install** → chờ cài xong → chọn **Reboot now** hoặc **I want to manually reboot later** → **Finish**.

### Bước 2.3. Mở IntelliJ lần đầu

1. Mở IntelliJ IDEA.
2. Đồng ý điều khoản (**User Agreement**) → Continue.
3. Chọn có/không gửi dữ liệu thống kê (tùy ý).
4. Nếu được hỏi chọn giao diện (theme) → chọn tùy thích.
5. Màn hình **Welcome to IntelliJ IDEA** xuất hiện là cài thành công.

### Bước 2.4. (Khuyến nghị) Cài plugin hỗ trợ

Ở màn hình Welcome → **Plugins** → tab **Marketplace**, tìm và cài (không bắt buộc):
- **Lombok** (thường đã có sẵn) — sẽ dùng sau này để viết code ngắn hơn.

✅ Mở được IntelliJ → xong Phần 2.

---

## Phần 3. Tạo project Spring Boot và chạy "Hello World"

### Bước 3.1. Tạo project trên Spring Initializr

1. Vào: **https://start.spring.io**
2. Điền thông tin như bảng sau:

| Mục | Chọn / Điền | Giải thích |
|---|---|---|
| **Project** | `Maven` | Công cụ build, project dùng Maven |
| **Language** | `Java` | |
| **Spring Boot** | Bản mới nhất **không** có chữ `SNAPSHOT` hoặc `M1/RC` | Bản ổn định |
| **Group** | `com.miniecommerce` | Thường là tên miền đảo ngược |
| **Artifact** | `backend` | Tên project |
| **Name** | `backend` | |
| **Description** | `Mini E-commerce Backend` | |
| **Package name** | `com.miniecommerce.backend` | Tự sinh, để nguyên |
| **Packaging** | `Jar` | |
| **Configuration** | `Properties` | Dùng file `application.properties` |
| **Java** | `17` | ⚠️ Phải khớp với JDK đã cài |

3. Bên phải, bấm **ADD DEPENDENCIES...** → gõ `web` → chọn **Spring Web**.
   *(Lúc này chỉ thêm **Spring Web** thôi. Phần kết nối MySQL sẽ thêm ở Phần 6 — nếu thêm ngay bây giờ, ứng dụng sẽ báo lỗi vì chưa có database.)*
4. Bấm **GENERATE** (hoặc `Ctrl + Enter`) → trình duyệt tải về file `backend.zip`.

### Bước 3.2. Giải nén vào thư mục `backend/` của repository

1. Giải nén `backend.zip` → được thư mục `backend` chứa: `src/`, `pom.xml`, `mvnw`, `mvnw.cmd`, `.mvn/`, `.gitignore`, `HELP.md`.
2. **Copy toàn bộ nội dung bên trong** thư mục vừa giải nén vào thư mục `backend/` của repository `mini-e-commerce`.
   ⚠️ Hiện Windows đang ẩn file/thư mục bắt đầu bằng dấu chấm (`.mvn`, `.gitignore`)? Trong File Explorer chọn **View → Show → Hidden items** để chắc chắn copy đủ.
3. Kết quả đúng phải là:

```text
mini-e-commerce/
├── backend/
│   ├── .mvn/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/miniecommerce/backend/BackendApplication.java
│   │   │   └── resources/application.properties
│   │   └── test/
│   ├── .gitignore
│   ├── details.md          (file cũ có sẵn, giữ nguyên)
│   ├── HELP.md
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml             ⚠️ pom.xml phải nằm NGAY trong backend/, không phải backend/backend/pom.xml
├── docs/
└── ...
```

### Bước 3.3. Mở project bằng IntelliJ

1. Mở IntelliJ → **Open** (hoặc **File → Open...**).
2. Chọn **thư mục `backend`** (thư mục có chứa `pom.xml`) → **OK**.
3. Nếu hỏi **Trust Project?** → **Trust Project**.
4. Chờ IntelliJ tải thư viện (thanh tiến trình ở góc dưới bên phải, lần đầu có thể mất vài phút tùy mạng). **Đợi chạy xong hẳn** rồi mới làm tiếp.

### Bước 3.4. Kiểm tra IntelliJ đang dùng đúng JDK 17

1. Vào **File → Project Structure...** (`Ctrl + Alt + Shift + S`).
2. Mục **Project**:
   - **SDK**: chọn `17` (Eclipse Temurin). Nếu chưa có trong danh sách → **Add SDK → JDK...** → chọn thư mục JDK ở `C:\Program Files\Eclipse Adoptium\jdk-17...`.
   - **Language level**: `SDK default` hoặc `17`.
3. **OK**.
4. Vào **File → Settings → Build, Execution, Deployment → Build Tools → Maven → Runner** → mục **JRE** chọn `Use Project JDK` (hoặc JDK 17) → **OK**.

### Bước 3.5. Tạo Controller "Hello World"

1. Ở cửa sổ **Project** bên trái, mở: `src/main/java/com/miniecommerce/backend`
2. Chuột phải vào package `com.miniecommerce.backend` → **New → Package** → gõ `controller` → Enter.
   *(Theo `coding-convention.md`: code chia theo package `controller`, `service`, `repository`...)*
3. Chuột phải vào package `controller` vừa tạo → **New → Java Class** → gõ `HelloController` → Enter.
4. Xóa hết nội dung và dán code sau:

```java
package com.miniecommerce.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World from Mini E-commerce Backend!";
    }
}
```

Giải thích nhanh:
- `@RestController`: đánh dấu class này nhận HTTP request và trả dữ liệu về (không phải trả trang HTML).
- `@RequestMapping("/api")`: mọi API trong class này bắt đầu bằng `/api`.
- `@GetMapping("/hello")`: khi gọi `GET /api/hello` thì chạy hàm `hello()`.

### Bước 3.6. Chạy ứng dụng

**Cách 1 — Chạy trong IntelliJ (khuyên dùng):**

1. Mở file `BackendApplication.java` (file có hàm `main` và annotation `@SpringBootApplication`).
2. Bấm vào **nút tam giác xanh ▶** bên cạnh dòng `public class BackendApplication` → **Run 'BackendApplication'**.
3. Cửa sổ **Run** ở dưới hiện log. Chờ tới khi thấy dòng tương tự:

```text
Tomcat started on port 8080 (http) with context path '/'
Started BackendApplication in 2.345 seconds
```

**Cách 2 — Chạy bằng terminal (Maven Wrapper):**

Mở terminal trong IntelliJ (**View → Tool Windows → Terminal**, hoặc `Alt + F12`), đảm bảo đang ở thư mục `backend`, gõ:

```powershell
.\mvnw.cmd spring-boot:run
```

> 💡 Trong PowerShell phải có `.\` ở đầu. Lần đầu chạy, Maven Wrapper sẽ tự tải Maven về nên hơi lâu.

### Bước 3.7. Kiểm tra

Mở trình duyệt, vào: **http://localhost:8080/api/hello**

Kết quả mong đợi — trình duyệt hiện:

```text
Hello World from Mini E-commerce Backend!
```

Để **dừng** ứng dụng: bấm nút **ô vuông đỏ ■** trong IntelliJ, hoặc `Ctrl + C` trong terminal.

✅ Thấy dòng Hello World → xong Phần 3. 🎉

### Lỗi thường gặp — Phần 3

| Hiện tượng | Cách xử lý |
|---|---|
| `Web server failed to start. Port 8080 was already in use.` | Có ứng dụng khác đang dùng port 8080 (thường là chính app cũ chưa tắt). Tắt app cũ; hoặc thêm dòng `server.port=8081` vào `application.properties` rồi vào `http://localhost:8081/api/hello`. |
| `release version 17 not supported` / `invalid target release: 17` | IntelliJ/Maven đang dùng JDK cũ. Làm lại **Bước 3.4**, và kiểm tra `JAVA_HOME`. |
| Code báo đỏ `Cannot resolve symbol 'springframework'` | Maven chưa tải xong thư viện. Mở tab **Maven** (bên phải) → bấm nút **Reload All Maven Projects** (biểu tượng 2 mũi tên xoay vòng). |
| Vào trình duyệt ra trang `Whitelabel Error Page` 404 | Sai URL, hoặc class `HelloController` không nằm trong package con của `com.miniecommerce.backend`. |
| `'.\mvnw.cmd' is not recognized` | Terminal không đứng ở thư mục `backend`. Gõ `cd backend` rồi thử lại. |

---

## Phần 4. Cài MySQL Server + MySQL Workbench

### Bước 4.1. Kiểm tra máy đã có MySQL chưa

Bấm `Windows`, gõ **`services`** → mở **Services**. Tìm dòng bắt đầu bằng **`MySQL`** (ví dụ `MySQL84`, `MySQL80`).
- Nếu **có** và Status là `Running` → máy đã có MySQL. Nếu vẫn nhớ mật khẩu `root` thì có thể nhảy sang **Bước 4.4** (cài Workbench nếu chưa có).
- Nếu **không có** → làm tiếp Bước 4.2.

### Bước 4.2. Tải MySQL Server

1. Vào: **https://dev.mysql.com/downloads/mysql/**
2. Chọn phiên bản **8.4.x LTS** (mục *Select Version*), **Operating System**: `Microsoft Windows`.
3. Tải gói **Windows (x86, 64-bit), MSI Installer**.
4. Trang tiếp theo có nút đăng nhập/đăng ký → bấm dòng nhỏ **"No thanks, just start my download."** để tải luôn.

### Bước 4.3. Cài đặt MySQL Server

**a) Cài chương trình**

1. Chạy file `mysql-8.4.x-winx64.msi` → **Next**.
2. Đồng ý License → **Next**.
3. Chọn **Typical** → **Install** → chờ xong.
4. Ở màn hình cuối, để tick **Run MySQL Configurator** → **Finish**.

**b) Cấu hình bằng MySQL Configurator** (tự mở sau khi cài; nếu không mở thì tìm "MySQL Configurator" trong Start Menu)

1. Màn hình Welcome → **Next**.
2. **Data Directory**: để mặc định → **Next**.
3. **Type and Networking**:
   - **Config Type**: `Development Computer`
   - ☑ **TCP/IP**, **Port**: `3306` ⚠️ nhớ số port này
   - ☑ **Open Windows Firewall ports for network access**
   - → **Next**
4. **Accounts and Roles**:
   - Đặt **MySQL Root Password** và nhập lại ở **Repeat Password**.
   - ⚠️ **GHI LẠI MẬT KHẨU NÀY** (ví dụ vào sổ tay). Quên mật khẩu root rất phiền.
   - Chưa cần tạo user ở đây (sẽ tạo bằng lệnh SQL ở Phần 5) → **Next**.
5. **Windows Service**:
   - ☑ **Configure MySQL Server as a Windows Service**
   - **Windows Service Name**: để mặc định (ví dụ `MySQL84`)
   - ☑ **Start the MySQL Server at System Startup** (tự chạy khi bật máy)
   - **Run Windows Service as**: `Standard System Account`
   - → **Next**
6. **Server File Permissions**: để mặc định → **Next**.
7. **Sample Databases**: không cần tick → **Next**.
8. **Apply Configuration** → bấm **Execute** → chờ tất cả bước hiện dấu tick xanh ✅ → **Next** → **Finish**.

### Bước 4.4. Cài MySQL Workbench (giao diện đồ họa)

1. Vào: **https://dev.mysql.com/downloads/workbench/**
2. **Operating System**: `Microsoft Windows` → tải bản **MSI Installer**.
3. Bấm **"No thanks, just start my download."**
4. Chạy file vừa tải → **Next** → **Complete** → **Install** → **Finish**.

> Nếu khi cài Workbench báo thiếu **Microsoft Visual C++ Redistributable** → tải bản **x64** tại https://learn.microsoft.com/cpp/windows/latest-supported-vc-redist, cài xong rồi cài lại Workbench.

### Bước 4.5. (Tùy chọn) Thêm `mysql` vào PATH để dùng lệnh trong terminal

Làm giống Bước 1.4 (d): mở **Environment Variables** → **System variables** → `Path` → **Edit** → **New** → thêm:

```text
C:\Program Files\MySQL\MySQL Server 8.4\bin
```

(Kiểm tra đúng tên thư mục trong `C:\Program Files\MySQL\` trên máy em.) → **OK** hết. Mở **terminal mới**, gõ:

```powershell
mysql --version
```

Kết quả mong đợi: `mysql  Ver 8.4.x for Win64 on x86_64 (MySQL Community Server - GPL)`

### Bước 4.6. Kiểm tra kết nối bằng Workbench

1. Mở **MySQL Workbench**.
2. Màn hình chính có ô **Local instance MySQL84** (hoặc tương tự) → click vào.
   *(Nếu không có: bấm dấu **⊕** cạnh chữ "MySQL Connections" → Connection Name: `local`, Hostname: `127.0.0.1`, Port: `3306`, Username: `root` → **OK**.)*
3. Nhập mật khẩu root đã đặt ở Bước 4.3 → có thể tick **Save password in vault** → **OK**.
4. Mở được cửa sổ soạn SQL (có khung **Query 1**) là kết nối thành công.

Gõ thử vào khung Query:

```sql
SELECT VERSION();
```

Bấm biểu tượng **tia sét ⚡** (Execute) hoặc `Ctrl + Enter`. Kết quả mong đợi: hiện `8.4.x`.

✅ Xong Phần 4.

### Lỗi thường gặp — Phần 4

| Hiện tượng | Cách xử lý |
|---|---|
| Workbench báo `Can't connect to MySQL server on '127.0.0.1:3306'` | MySQL chưa chạy. Mở **Services** → tìm `MySQL84` → chuột phải → **Start**. |
| `Access denied for user 'root'@'localhost'` | Sai mật khẩu root. |
| Lúc cài báo port 3306 đã bị dùng | Máy đã có MySQL khác (hoặc XAMPP). Dùng MySQL có sẵn đó, hoặc đổi port (ví dụ `3307`) và nhớ dùng port mới ở Phần 6. |

---

## Phần 5. Tạo database (schema) và user cho project

> ❓ **Tại sao không dùng luôn user `root`?** `root` có toàn quyền trên MySQL. Thực tế, mỗi ứng dụng nên có **user riêng**, chỉ có quyền trên đúng database của nó — nếu lộ mật khẩu thì thiệt hại cũng ít hơn.

### Bước 5.1. Tạo database `mini_ecommerce`

Trong **MySQL Workbench** (đăng nhập bằng `root`), dán vào khung Query và chạy (`Ctrl + Shift + Enter` để chạy tất cả):

```sql
CREATE DATABASE IF NOT EXISTS mini_ecommerce
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

- `utf8mb4`: để lưu được **tiếng Việt có dấu** và cả emoji. ⚠️ Không bỏ qua dòng này.

### Bước 5.2. Tạo user riêng cho ứng dụng

Thay `DoiMatKhauO_Day_123` bằng mật khẩu em tự đặt (và **ghi lại**):

```sql
CREATE USER 'mini_ecommerce_app'@'localhost' IDENTIFIED BY 'DoiMatKhauO_Day_123';

GRANT ALL PRIVILEGES ON mini_ecommerce.* TO 'mini_ecommerce_app'@'localhost';

FLUSH PRIVILEGES;
```

- `'mini_ecommerce_app'@'localhost'`: user tên `mini_ecommerce_app`, chỉ được đăng nhập từ chính máy này.
- `ON mini_ecommerce.*`: chỉ có quyền trên các bảng của database `mini_ecommerce`.

### Bước 5.3. Tạo bảng thử để kiểm tra kết nối

Bảng này **chỉ dùng để thử**, không nằm trong thiết kế ERD. Sau khi test xong ở Phần 6 có thể xóa.

```sql
USE mini_ecommerce;

CREATE TABLE connection_test (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  message    VARCHAR(255) NOT NULL,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO connection_test (message) VALUES ('Kết nối MySQL thành công - xin chào từ database!');

SELECT * FROM connection_test;
```

Kết quả mong đợi: bảng kết quả hiện 1 dòng với nội dung tiếng Việt hiển thị đúng dấu.

### Bước 5.4. Kiểm tra đăng nhập bằng user mới

1. Về màn hình chính của Workbench (biểu tượng **ngôi nhà** góc trên trái).
2. Bấm **⊕** cạnh "MySQL Connections":
   - **Connection Name**: `mini_ecommerce_app`
   - **Hostname**: `127.0.0.1`, **Port**: `3306`
   - **Username**: `mini_ecommerce_app`
   - **Default Schema**: `mini_ecommerce`
3. Bấm **Test Connection** → nhập mật khẩu của user app → phải hiện **"Successfully made the MySQL connection"** → **OK** → **OK**.
4. Mở connection này, chạy:

```sql
SELECT * FROM connection_test;
```

Phải thấy lại dòng dữ liệu vừa insert.

✅ Xong Phần 5.

---

## Phần 6. Kết nối Spring Boot với MySQL

### Bước 6.1. Thêm thư viện JPA và MySQL Driver vào `pom.xml`

Cách này đảm bảo lấy đúng tên thư viện cho phiên bản Spring Boot em đang dùng:

1. Quay lại **https://start.spring.io**, điền **giống hệt Bước 3.1** (cùng phiên bản Spring Boot đã chọn lúc trước).
2. **ADD DEPENDENCIES** → thêm **Spring Data JPA** và **MySQL Driver** (giữ cả **Spring Web**).
3. Bấm nút **EXPLORE** (hoặc `Ctrl + Space`) → hiện nội dung file `pom.xml`.
4. Tìm trong phần `<dependencies>` 2 khối liên quan tới **JPA** và **MySQL**, thường trông như sau:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

5. Mở file `backend/pom.xml` trong IntelliJ → dán 2 khối đó vào **bên trong** thẻ `<dependencies> ... </dependencies>` (đặt cạnh các `<dependency>` đã có).
6. Lưu file (`Ctrl + S`). Góc trên bên phải editor sẽ hiện biểu tượng **Maven nhỏ (chữ m + mũi tên xoay)** → bấm vào để **Load Maven Changes** (hoặc tab **Maven** → **Reload All Maven Projects**). Chờ tải xong.

> 💡 Nếu nội dung ở trang EXPLORE khác một chút so với ví dụ trên thì **dùng theo trang EXPLORE** — đó là bản đúng với phiên bản Spring Boot em chọn.

### Bước 6.2. Cấu hình kết nối trong `application.properties`

Mở `src/main/resources/application.properties`, thay toàn bộ nội dung bằng:

```properties
spring.application.name=backend

# ===== Server =====
server.port=8080

# ===== Database (MySQL) =====
# Giá trị lấy từ biến môi trường. Phần sau dấu ":" là giá trị mặc định nếu không set biến.
# KHÔNG ghi mật khẩu thật vào file này (file này được commit lên Git).
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/mini_ecommerce}
spring.datasource.username=${DB_USERNAME:mini_ecommerce_app}
spring.datasource.password=${DB_PASSWORD}

# ===== JPA / Hibernate =====
# none: Hibernate KHÔNG tự tạo/sửa bảng. Bảng được tạo bằng SQL script.
spring.jpa.hibernate.ddl-auto=none
# In câu SQL ra log để dễ học/debug
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

Giải thích:
- `${DB_PASSWORD}`: Spring sẽ đọc **biến môi trường** tên `DB_PASSWORD`. Mật khẩu **chỉ nằm trên máy em**, không nằm trong code → đúng quy tắc bảo mật trong `environment-setup.md`.
- `ddl-auto=none`: an toàn nhất cho người mới. (Các giá trị khác như `update`, `create` sẽ tìm hiểu sau khi làm Entity ở Tuần 2 — cần thống nhất với anh trước khi đổi.)

### Bước 6.3. Khai báo biến môi trường `DB_PASSWORD` trong IntelliJ

1. Trên thanh công cụ góc trên bên phải, bấm vào ô tên cấu hình chạy **BackendApplication** → chọn **Edit Configurations...**
2. Chọn **BackendApplication** (mục Application / Spring Boot bên trái).
3. Tìm ô **Environment variables**.
   *(Nếu không thấy: bấm **Modify options** → tick **Environment variables**.)*
4. Điền:

```text
DB_PASSWORD=DoiMatKhauO_Day_123
```

   (Thay bằng mật khẩu của user `mini_ecommerce_app` ở Bước 5.2. Có nhiều biến thì ngăn cách bằng dấu `;`.)
5. **Apply** → **OK**.

> ⚠️ Cấu hình chạy này được lưu trong thư mục `.idea/` — thư mục này đã nằm trong `backend/.gitignore` (do Spring Initializr tạo sẵn) nên **không bị commit**. Kiểm tra lại: mở `backend/.gitignore`, phải thấy dòng `.idea`.

**Nếu chạy bằng terminal (`mvnw`)** thì set biến trong PowerShell trước khi chạy (chỉ có hiệu lực trong cửa sổ terminal đó):

```powershell
$env:DB_PASSWORD = "DoiMatKhauO_Day_123"
.\mvnw.cmd spring-boot:run
```

### Bước 6.4. Tạo API kiểm tra kết nối database

Tạo class mới trong package `controller`: chuột phải `controller` → **New → Java Class** → `DatabaseCheckController`, dán code:

```java
package com.miniecommerce.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller tạm thời để kiểm tra kết nối MySQL. Xóa khi đã làm xong các API thật.
@RestController
@RequestMapping("/api/db-check")
public class DatabaseCheckController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public Map<String, Object> check() {
        String databaseName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        String mysqlVersion = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM connection_test");

        return Map.of(
                "status", "CONNECTED",
                "database", databaseName,
                "mysqlVersion", mysqlVersion,
                "connectionTestRows", rows
        );
    }
}
```

Giải thích:
- `JdbcTemplate`: công cụ của Spring để chạy câu SQL. Spring **tự tạo** và **tự truyền vào** constructor (gọi là *Dependency Injection*) dựa trên cấu hình ở Bước 6.2.
- API này chạy 3 câu SQL và trả kết quả dạng JSON.

### Bước 6.5. Chạy lại và kiểm tra

1. Dừng app cũ (nếu đang chạy) → chạy lại **BackendApplication** (▶).
2. Trong log, phải thấy các dòng tương tự:

```text
HikariPool-1 - Starting...
HikariPool-1 - Added connection com.mysql.cj.jdbc.ConnectionImpl@...
HikariPool-1 - Start completed.
...
Started BackendApplication in 4.123 seconds
```

   (`HikariPool` là bộ quản lý kết nối database. "Start completed" = kết nối MySQL thành công.)
3. Mở trình duyệt: **http://localhost:8080/api/db-check**

Kết quả mong đợi (thứ tự các trường có thể khác):

```json
{
  "status": "CONNECTED",
  "database": "mini_ecommerce",
  "mysqlVersion": "8.4.x",
  "connectionTestRows": [
    {
      "id": 1,
      "message": "Kết nối MySQL thành công - xin chào từ database!",
      "created_at": "2026-09-29T10:00:00"
    }
  ]
}
```

4. Vào lại **http://localhost:8080/api/hello** — vẫn phải hiện Hello World.

✅ Thấy `"status": "CONNECTED"` và dòng dữ liệu tiếng Việt đúng dấu → **Spring Boot đã kết nối MySQL thành công**. 🎉

### Lỗi thường gặp — Phần 6

| Hiện tượng (trong log) | Nguyên nhân / Cách xử lý |
|---|---|
| `Access denied for user 'mini_ecommerce_app'@'localhost' (using password: NO)` | Chưa set biến `DB_PASSWORD`. Làm lại **Bước 6.3**. |
| `Access denied ... (using password: YES)` | Sai mật khẩu hoặc sai username. Kiểm tra lại bằng Workbench (Bước 5.4). |
| `Could not resolve placeholder 'DB_PASSWORD'` | Chưa set biến `DB_PASSWORD` (Bước 6.3). |
| `Communications link failure` / `Connection refused` | MySQL chưa chạy (vào **Services** → Start `MySQL84`), hoặc sai port trong `DB_URL`. |
| `Unknown database 'mini_ecommerce'` | Chưa tạo database hoặc gõ sai tên. Làm lại **Bước 5.1**. |
| `Table 'mini_ecommerce.connection_test' doesn't exist` | Chưa tạo bảng thử. Làm lại **Bước 5.3**. |
| `Public Key Retrieval is not allowed` | Thêm tham số vào URL: đặt biến môi trường `DB_URL=jdbc:mysql://localhost:3306/mini_ecommerce?allowPublicKeyRetrieval=true&useSSL=false` (chỉ dùng cho môi trường local). |
| `Failed to configure a DataSource: 'url' attribute is not specified` | `application.properties` chưa được lưu hoặc gõ sai tên thuộc tính. |
| `Cannot resolve symbol 'JdbcTemplate'` | Chưa reload Maven sau khi sửa `pom.xml` (Bước 6.1 ý 6). |
| Tiếng Việt bị lỗi font `K?t n?i` | Database chưa tạo với `utf8mb4` (Bước 5.1). |

---

## Phần 7. Commit lên Git

Tuân theo `docs/04-development/git-convention.md` (tạo branch, đặt tên commit theo quy ước của project).

### Bước 7.1. Kiểm tra trước khi commit

Ở thư mục gốc `mini-e-commerce`, chạy:

```powershell
git status
```

Kiểm tra danh sách file sẽ commit:
- ✅ **Được** commit: `backend/src/...`, `backend/pom.xml`, `backend/mvnw`, `backend/mvnw.cmd`, `backend/.mvn/`, `backend/.gitignore`.
- ❌ **Không được** xuất hiện: `backend/target/`, `backend/.idea/`, file chứa mật khẩu.

Tìm nhanh xem có lỡ ghi mật khẩu vào code không (thay bằng mật khẩu thật của em):

```powershell
git grep -n "DoiMatKhauO_Day_123"
```

Không in ra gì = an toàn.

### Bước 7.2. Commit

```powershell
git checkout -b feature/setup-backend
git add backend
git commit -m "chore(backend): init Spring Boot project and MySQL connection"
git push -u origin feature/setup-backend
```

(Tên branch/commit điều chỉnh theo `git-convention.md` nếu quy ước khác.)

---

## Checklist cuối cùng

### Java & IDE
- [x] `java -version` ra phiên bản **17**.
- [x] `javac -version` ra phiên bản **17**.
- [x] `echo $env:JAVA_HOME` in ra đúng thư mục JDK 17.
- [x] IntelliJ mở được project `backend`, Project SDK = 17.

### Spring Boot
- [x] Project được tạo từ start.spring.io, `pom.xml` nằm ngay trong `backend/`.
- [x] Chạy app không lỗi, log có `Started BackendApplication`.
- [x] `http://localhost:8080/api/hello` trả về Hello World.

### MySQL
- [x] MySQL Server đang chạy (Services → `MySQL84` = Running).
- [x] Đăng nhập được Workbench bằng `root`.
- [x] Database `mini_ecommerce` đã tạo với `utf8mb4`.
- [x] User `mini_ecommerce_app` đăng nhập được, chỉ có quyền trên `mini_ecommerce`.
- [x] Bảng `connection_test` có dữ liệu.

### Kết nối
- [x] Log có `HikariPool-1 - Start completed`.
- [x] `http://localhost:8080/api/db-check` trả về `"status": "CONNECTED"`.
- [x] Mật khẩu database **không** nằm trong bất kỳ file nào được commit.

### Việc tiếp theo (Tuần 2)
Sau khi hoàn thành hướng dẫn này, em đã xong task **"Setup Backend"** và **"Database & JPA"** (phần kết nối) trong `docs/05-planning/danh-muc-dau-viec-tuan-2-6.md`. Bước tiếp theo:
- Tạo cấu trúc package theo `coding-convention.md` (`controller`, `service`, `repository`, `entity`, `dto`, `exception`, `config`...).
- Viết script tạo bảng theo ERD (`docs/02-design/erd/`) và lưu vào thư mục `database/`.
- Tạo Entity + Repository đầu tiên (`Category`, `Product`).
- Khi đã có API thật: **xóa** `DatabaseCheckController` và bảng `connection_test` (`DROP TABLE connection_test;`).

---

## Ghi chú về thời gian ước tính

| Phần | Thời gian ước tính (người mới) |
|---|---|
| Phần 1 — Java | 20–30 phút |
| Phần 2 — IntelliJ | 15–20 phút |
| Phần 3 — Hello World | 30–45 phút |
| Phần 4 — MySQL | 30–45 phút |
| Phần 5 — Database & user | 15 phút |
| Phần 6 — Kết nối | 30–45 phút |

> Gặp lỗi không nằm trong các bảng "Lỗi thường gặp": **copy toàn bộ thông báo lỗi** (đặc biệt là dòng có chữ `Caused by:` cuối cùng trong log) và gửi lại để cùng xem. Đừng chỉ chụp một phần màn hình.
