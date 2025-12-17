# How to start from scratch

1. Download and install Window Builder on Eclipse Marketplace
2. Create a `Data`sub directory under your root.
3. Move `rs2xml.jar`, `sqlite.jar` and `sqlitejdbc.jar` files into the Data directory.
4. Open Google Chrome and install `SQLite Manager` extension from the Google Chrome Marketplace.
5. Create a new database on Chrome using the new extension and download it.
6. Move the file you downloaded to your Data sub-directory.
7. On Eclipse, right-click your root directory. Go to `Build Path` -> `Configure Build Path...`
8. Under Java Build Path go to Libraries and click on Modulepath. Click `Add JARs...` and choose your `sqlite.jar` and `sqlitejdbc.jar` files, click Ok. Now, click on Classpath, click `Add JARs...` choose the `rs2xml.jar` file, click Ok. Click `Apply and Close`.
9. Create a Package under src directory.
10. After you right-click the package, go to New -> Other.
11. Under Window Builder -> Swing Designer choose JFrame and click Next. You can start building your app now.
12. When you want to re-open a previously written JFrame code, right-click the file and choose Open With and choose WindowBuilder Editor.