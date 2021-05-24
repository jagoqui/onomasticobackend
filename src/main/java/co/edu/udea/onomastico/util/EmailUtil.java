package co.edu.udea.onomastico.util;

public class EmailUtil {

    public static String emailContent(String plantilla, String unsubscribeEmail, String email, String IMAGE_SERVER) {
        String body = "<div id=\"wrapper-mail\" style=\"background: #f2f2f2; margin: 0; padding: 0; font-family: Roboto, RobotoDraft, Helvetica, Arial, sans-serif; color: #698391;\">\n" +
                "    <div id=\"mail-container\"\n" +
                "         style=\"background: #ffffff; margin: 0 auto; max-width: 650px; text-align: center; font-size: calc(0.75em + 1vmin);\">\n" +
                "        <div id=\"caption\" style=\"background: #f2f2f2; color: rgb(102, 102, 102); font-size: 50%; padding: 10px;\">\n" +
                "            Para asegurar la entrega de nuestros e-mail en su correo, por favor agregue\n" +
                "            <span id=\"mail-address\"\n" +
                "                  style=\"color: #87b037; text-decoration: none;\"> aplicaciononomastico@udea.edu.co </span>a su libreta\n" +
                "            de direcciones de correo.<br>\n" +
                "            Si usted no visualiza bien este mail, haga <a id=\"mail-in-db\" href=\"#\"\n" +
                "                                                          style=\"cursor: pointer; color: #87b037; font-weight: bold; text-decoration: none;\">click\n" +
                "            aquí</a>\n" +
                "        </div>\n" +
                "        <img id=\"img-logo\" alt=\"logo UdeA\"\n" +
                "             src="+IMAGE_SERVER.concat("logo-udea.png")+
                "             style=\"width: 100%; background-color: #0c5a31;\">\n" +
                "        <div id=\"template-card-container\"\n" +
                "             style=\"padding: 2vw; text-align: justify; text-justify: inter-word; display: inline-block;\">\n" +
                "            <!--***********************************AQUÍ VA LA PLANTILLA *****************************************-->\n"
                + plantilla +
                "        </div>\n" +
                "        <div id=\"bar-line\"\n" +
                "             style=\"margin-top: 6vw; height: 1vw; min-height: 5px; max-height: 15px; background: #87b037;\"></div>\n" +
                "        <div id=\"social-media\" style=\"padding: 5%; background: white; margin: 0 auto;\">\n" +
                "            <a href=\"https://www.facebook.com/universidaddeantioquia\" target=\"_blank\"\n" +
                "               data-saferedirecturl=\"https://www.google.com/url?q=https://www.facebook.com/universidaddeantioquia&amp;source=gmail&amp;ust=1621018605357000&amp;usg=AFQjCNFtvziW4oXFQ40YdOzurH1Tmy0Qmg\"\n" +
                "               style=\"font-weight: bold; cursor: pointer; color: white; padding: 0.4vw; text-decoration: none;\">\n" +
                "                <img alt=\"FACEBOOK\"\n" +
                "                     src="+IMAGE_SERVER.concat("facebook.png") +
                "                     style=\"width: 8vw; min-width: 24px; max-width: 30px; height: auto;\">\n" +
                "            </a>\n" +
                "            <a href=\"https://twitter.com/UdeA\" target=\"_blank\"\n" +
                "               data-saferedirecturl=\"https://www.google.com/url?q=https://twitter.com/UdeA&amp;source=gmail&amp;ust=1621018605357000&amp;usg=AFQjCNHAlc_MSz-EmnasuDPX_euorSxPeA\"\n" +
                "               style=\"font-weight: bold; cursor: pointer; color: white; padding: 0.4vw; text-decoration: none;\"><img\n" +
                "                    alt=\"TWITTER\"\n" +
                "                    src="+IMAGE_SERVER.concat("twitter.png")+
                "                    style=\"width: 8vw; min-width: 24px; max-width: 30px; height: auto;\"></a>\n" +
                "            <a href=\"https://www.youtube.com/user/UniversidadAntioquia\" target=\"_blank\"\n" +
                "               data-saferedirecturl=\"https://www.google.com/url?q=https://www.youtube.com/user/UniversidadAntioquia&amp;source=gmail&amp;ust=1621018605357000&amp;usg=AFQjCNG-qhOt5lXXOnZkZeVJpXHuhvFBLw\"\n" +
                "               style=\"font-weight: bold; cursor: pointer; color: white; padding: 0.4vw; text-decoration: none;\">\n" +
                "                <img alt=\"youtube\"\n" +
                "                     src="+IMAGE_SERVER.concat("youtube.png")+
                "                     style=\"width: 8vw; min-width: 24px; max-width: 30px; height: auto;\"></a>\n" +
                "            <a href=\"https://co.linkedin.com/school/universidad-de-antioquia/\" target=\"_blank\"\n" +
                "               data-saferedirecturl=\"https://www.google.com/url?q=https://co.linkedin.com/school/universidad-de-antioquia/&amp;source=gmail&amp;ust=1621018605357000&amp;usg=AFQjCNGXPLtbCZXGLUh7pW_YJNNHk0dcWQ\"\n" +
                "               style=\"font-weight: bold; cursor: pointer; color: white; padding: 0.4vw; text-decoration: none;\"><img\n" +
                "                    alt=\"linkedin\"\n" +
                "                    src="+IMAGE_SERVER.concat("linkedin.png")+
                "                    style=\"width: 8vw; min-width: 24px; max-width: 30px; height: auto;\"></a>\n" +
                "            <a href=\"https://www.instagram.com/udea/\" target=\"_blank\"\n" +
                "               data-saferedirecturl=\"https://www.google.com/url?q=https://www.instagram.com/udea/&amp;source=gmail&amp;ust=1621018605357000&amp;usg=AFQjCNFvd3Uypesh7TQGarv87yC085w22Q\"\n" +
                "               style=\"font-weight: bold; cursor: pointer; color: white; padding: 0.4vw; text-decoration: none;\"><img\n" +
                "                    alt=\"instagram\"\n" +
                "                    src="+IMAGE_SERVER.concat("instagram.png")+
                "                    style=\"width: 8vw; min-width: 24px; max-width: 30px; height: auto;\"></a>\n" +
                "            <a href=\"https://soundcloud.com/universidaddeantioquia\" target=\"_blank\"\n" +
                "               data-saferedirecturl=\"https://www.google.com/url?q=https://soundcloud.com/universidaddeantioquia&amp;source=gmail&amp;ust=1621018605357000&amp;usg=AFQjCNFaHSLmAC1XLr2uy93YMIBApYZIPw\"\n" +
                "               style=\"font-weight: bold; cursor: pointer; color: white; padding: 0.4vw; text-decoration: none;\"><img\n" +
                "                    alt=\"soundcloud\"\n" +
                "                    src="+IMAGE_SERVER.concat("soundcloud.png")+
                "                    style=\"width: 8vw; min-width: 24px; max-width: 30px; height: auto;\"></a>\n" +
                "        </div>\n" +
                "        <div id=\"footer\" style=\"background: #0c5a31; color: white; padding: 1vw; font-size: 55%; line-height: 15px;\">\n" +
                "            <span style=\"display: block;\">Universidad de Antioquia | Vigilada Mineducación | Acreditación institucional hasta el 2022 | NIT 890980040-8 </span>\n" +
                "            <span style=\"display: block;\">\n" +
                "\t\t\t\t\t<a href=\"https://www.udea.edu.co\" target=\"_blank\"\n" +
                "                       data-saferedirecturl=\"https://www.google.com/url?q=https://www.udea.edu.co&amp;source=gmail&amp;ust=1621018605357000&amp;usg=AFQjCNFxCccV-znVSLbPMI00PJqqWfioHg\"\n" +
                "                       style=\"font-weight: bold; cursor: pointer; color: white; text-decoration: underline;\">\n" +
                "\t\t\t\t\twww.udea.edu.co</a>\n" +
                "\t\t\t\t\t| Líneas de Atención al Ciudadano: 01 8000 416384 [+57-4] 2198332 |\n" +
                "\t\t\t\t</span>\n" +
                "            <a href=\"https://atencionciudadano@udea.edu.co\" target=\"_blank\"\n" +
                "               data-saferedirecturl=\"https://www.google.com/url?q=https://atencionciudadano@udea.edu.co&amp;source=gmail&amp;ust=1621018605357000&amp;usg=AFQjCNF33BV2SRzXXTf7uexm-Fp_BhN-cA\"\n" +
                "               style=\"font-weight: bold; cursor: pointer; color: white; text-decoration: underline;\">\n" +
                "                atencionciudadano@udea.edu.co</a>.\n" +
                "        </div>\n" +
                "        <div id=\"unsubscribe\" style=\"padding: 30px; font-size: 50%; color: rgb(102, 102, 102); line-height: 12px;\">\n" +
                "\t\t\t\t<span>\n" +
                "\t\t\t\t\tEste mensaje ha sido enviado a\n" +
                "\t\t\t\t\t<a id=\"mail-user\" href=\"mailto:" +email+ "target=\"_blank\"\n" +
                "                       style=\"cursor: pointer; font-weight: bold; color: #87b037; text-decoration: none;\">\n" +
                "\t\t\t\t\t\tcorreo@udea.edu.co\n" +
                "\t\t\t\t\t</a> desde Onomástico.<br>\n" +
                "\t\t\t\t\tPara anular su suscripción, haga\n" +
                "\t\t\t\t\t<a id=\"unsubscribe-user\" href=" + unsubscribeEmail+ "target=\"_blank\" data-saferedirecturl=\"#\"\n" +
                "                       style=\"cursor: pointer; font-weight: bold; color: #87b037; text-decoration: none;\">click aquí</a>\n" +
                "\t\t\t\t</span>\n" +
                "            <hr style=\"color: #BECCCC; width: 100%; margin: 1vw 0;\">\n" +
                "            <span>\n" +
                "\t\t\t\t\tEste mensaje fue enviado a través de <a id=\"domain-server\" href=\"\" target=\"_blank\"\n" +
                "                                                            data-saferedirecturl=\"\"\n" +
                "                                                            style=\"cursor: pointer; font-weight: bold; color: #87b037; text-decoration: none;\">Onomastico</a>\n" +
                "\t\t\t\t\tpor la Universidad de Antioquia\n" +
                "\t\t\t\t</span>\n" +
                "        </div>\n" +
                "        <div id=\"rate-us\" style=\"padding: 2vw;\">\n" +
                "            <a id=\"love_emoji\" href=\"#\" title=\"Presiona si te gusta este mail\"\n" +
                "               style=\"font-weight: bold; cursor: pointer; color: white; display: block; text-decoration: none;\">\uD83D\uDC9A</a>\n" +
                "            <span style=\"font-size: 55%; font-weight: bold;\">Me encanta!</span>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
        return body;
    }
}
