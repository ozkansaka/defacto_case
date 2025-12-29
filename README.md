# DeFacto Movie App

Bu proje, popüler filmleri ve TV şovlarını keşfetmenizi sağlayan bir mobil uygulamadır.

## Kurulum

Projeyi yerel makinenizde kurmak ve çalıştırmak için aşağıdaki adımları izleyin:

1.  **Projeyi klonlayın:**

    ```bash
    git clone https://github.com/ozkansaka/DeFactoMovieApp.git
    ```

2.  **Android Studio'da açın:**

    Projeyi Android Studio'da açın ve Gradle senkronizasyonunun tamamlanmasını bekleyin.

3.  **API Anahtarınızı Ekleyin:**

    Proje, film verilerini almak için bir API kullanmaktadır. API anahtarınızı `Constants` dosyasına eklemeniz gerekmektedir.

    ```constants
    API_KEY="SIZIN_API_ANAHTARINIZ"
    ```

4.  **Projeyi Çalıştırın:**

    Yapılandırmaları tamamladıktan sonra, projeyi bir emülatörde veya fiziksel bir cihazda çalıştırabilirsiniz.

## Mimari

Bu proje, aşağıdaki prensiplere dayanan modern bir Android uygulama mimarisi kullanır:

*   **Tek aktiviteli mimari (Single-Activity Architecture):** Uygulama, gezinme için Jetpack Navigation kullanan tek bir ana aktiviteye sahiptir.
*   **MVVM (Model-View-ViewModel):** Proje, kullanıcı arayüzünü iş mantığından ayırmak için MVVM mimari desenini kullanır.
*   **Jetpack Compose:** Kullanıcı arayüzü, Android'in modern bildirimsel kullanıcı arayüzü araç seti olan Jetpack Compose ile oluşturulmuştur.
*   **Dependency Injection:** Proje, bağımlılıkları yönetmek için Hilt kullanır.
*   **Asenkron Programlama:** Arka plan görevleri ve ağ işlemleri için Coroutines kullanılır.
*   **Veri Kalıcılığı:** Yerel veri depolama için Room ve DataStore kullanılır.
*   **Ağ (Networking):** Uzak verileri almak için Retrofit kullanılır.

![386](https://github.com/user-attachments/assets/6cf77dba-4472-4c60-b601-9313722426cb)
![387](https://github.com/user-attachments/assets/e1cfe85b-787a-4c6e-a660-dc9143139421)
![388](https://github.com/user-attachments/assets/8183bd39-1a80-450e-9e3b-a1752f9aa0a7)

