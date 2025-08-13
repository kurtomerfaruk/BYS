package tr.bel.gaziantep.bysweb.core.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tr.bel.gaziantep.bysweb.core.controller.InitApp;
import tr.bel.gaziantep.bysweb.core.utils.Constants;

import java.io.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 11:38
 */
@WebServlet("/flush")
public class ConnectionPoolServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -397194459158620673L;

    @Inject
    private InitApp initApp;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();


        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Connection Pool Flush Sonuçları</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 20px; }\n" +
                "        pre { background: #f5f5f5; padding: 10px; border-radius: 5px; }\n" +
                "        .success { color: green; }\n" +
                "        .error { color: red; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Connection Pool Flush İşlemi</h1>\n" +
                "    <h3>Pool Adı: " + Constants.POOL_NAME + "</h3>");

        try {
            String glassfishHome = System.getenv("GLASSFISH_HOME");
            if (glassfishHome == null) {
                glassfishHome = initApp.getProperty("glassfish.home");//"C:\\glassfish-7.0.21"; // Varsayılan yol
            }

            String asadminPath = glassfishHome + "\\bin\\asadmin.bat";
            File asadminFile = new File(asadminPath);

            if (!asadminFile.exists()) {
                out.println("<div class='error'><h3>Hata: asadmin.bat bulunamadı!</h3>");
                out.println("<p>Aranan yol: " + asadminPath + "</p></div>");
                return;
            }

            ProcessBuilder pb = new ProcessBuilder(
                    "cmd.exe", "/c",
                    asadminPath,
                    "flush-connection-pool",
                    Constants.POOL_NAME
            );

            pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
            pb.directory(new File(glassfishHome + "\\bin"));

            Process process = pb.start();

            // Çıktıları yakala
            String output = readStream(process.getInputStream());
            String error = readStream(process.getErrorStream());

            int exitCode = process.waitFor();

            out.println("<h3>İşlem Sonucu:</h3>");
            out.println("<p>Çıkış Kodu: " + exitCode + "</p>");

            if (!output.isEmpty()) {
                out.println("<h4>Çıktı:</h4>");
                out.println("<pre>" + output + "</pre>");
            }

            if (!error.isEmpty()) {
                out.println("<h4 class='error'>Hata Mesajı:</h4>");
                out.println("<pre class='error'>" + error + "</pre>");
            }

            if (exitCode == 0) {
                out.println("<div class='success'><h3>Başarılı: Pool başarıyla flush edildi</h3></div>");
            } else {
                out.println("<div class='error'><h3>Hata: Flush işlemi başarısız oldu</h3></div>");
            }

        } catch (Exception e) {
            out.println("<div class='error'><h3>Beklenmeyen Hata:</h3>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre></div>");
        }

        out.println("""
                    <br>
                    <a href='javascript:history.back()'>Geri Dön</a>
                </body>
                </html>
                """);
    }

    private String readStream(InputStream input) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }
}
