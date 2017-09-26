Esta Aplicación es del curso de EDX aplicaciones móviles desarrolladas con patrones y buenas prácticas de Programación, en este caso vamos a usar Inyección de dependencias
Vamos a mostrar Aplicación que muestra todos los archivos de imagenes y hashtags en nuestro muro gracias a Twitter ,

Commit1 :ConfigGradleLibraries


Pasos
    Vamos a https://apps.twitter.com/ y creamos una aplicación para trabajar en ella
    damos solo los permisos de lectura ya que vamos a solo a leer, mas no lo vamos a escribir ni publicar

    Editamos build.gradle, archivo general del proeyecto

    que vamos a modificar es el archivo general del proyecto, aquí vamos a agregar una referencia
    hacia el plugin de APT y este nos va servir para trabajar con la inyección de dependencias
    de "dagger2" es importante que esto tiene que ir aquí, con este plugin nos garantizamos
    que AndroidStudio va tomar los archivos generados de anotaciones, de esta forma funciona
    el inyector de dependencias que vamos a estar utilizando que es "dagger2"

    En TwitterApp
            classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'


    Adicional vamos a agregar como en los casos anteriores aquí todas las versiones que necesitamos,
    vamos a estar trabajando con:
                Butterknife
                EventBus
                Glide

                Dagger
                JSR250 para anotaciones
                TwitterVersion


    Como vemos agregamos a "dagger2", este requiere de JSR250 para las anotaciones, esto
    viene por defecto Java 7 pero Android funciona con Java 6,
    lo colocamos aquí.


    recordemos ponemos nuestras variables que deben de estar por defecto con respecto a las versiones de desarrolo

            "minSdk"
            "targetSdk"
            "compileSdk"
            "buildTools"
            "supportLibrary"



    Sincronizamos y tenemos lo siguiente

        ext {
            minSdkVersion = 16
            targetSdkVersion = 23
            compileSdkVersion = 23
            buildToolsVersion = '23.0.2'

            supportLibraryVersion = '23.1.1'


            twitterVersion = '1.12.0@aar'
            butterknifeVersion = '7.0.1'
            cloudinaryVersion = '1.1.2'
            eventbusVersion = '3.0.0'
            daggerVersion = '2.0.1'
            glideVersion = '3.6.1'
            jsr250Version = '1.0'
        }



    Luego al otro archivo de "build.gradle" (app) vamos hacer varias modificaciones, primero aplicamos
    un plugin de APT de respecto de lo hemos agregado antes, respecto a las anotaciones, debido a que si no ponemos este plugin las anotaciones no son reconocidas
                apply plugin: 'com.neenbedankt.android-apt'



    luego vamos a reemplazar
    aquí los valores que ya conocemos del "RootProject" reemplazamos todo esto, tal como lo hemos
    hecho en los proyectos anteriores "RootProject.ext" y las versiones que estoy leyendo y "BuildTypes"


            compileSdkVersion rootProject.ext.compileSdkVersion
            buildToolsVersion rootProject.ext.buildToolsVersion
            defaultConfig {
                applicationId "ec.edu.lexus.twitterapp"
                minSdkVersion rootProject.ext.minSdkVersion
                targetSdkVersion rootProject.ext.targetSdkVersion
                versionCode 1
                versionName "1.0"
                testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
            }



    vamos hacer una modificación un poco mayor la idea aquí es que mientras es que pueda leer desde un archivo de "gradle" los
    "KEYS" para Twitter,
    Para este caso voy utilizar "gradle.properties" pero podría utilizar algún otro archivo.
    entonces aquí voy a agregar mis credenciales de Twitter, aquí puedo definir variables,
    por ejemplo: "TWITTER_KEY" = pongo el valor de una variable etcétera, mis variables yo
    ya las tengo definidas lo obtuve de solicitar una aplicación en "Twitter" tenemos un "TwitterKey"
    y un "TwitterSecret"que van a ir aquí, va ver algo así, con mis variables, sin embargo,
    esos valores no son una buena idea, que estén en el control de versiones o que sean públicos,
    sino que son únicamente para ustedes, por lo que debemos editar el archivo .gitignore para evitar  que este archivo este dentro del control de versiones

        gradle.properties
                # org.gradle.parallel=true
                TWITTER_KEY="..."
                TWITTER_SECRET="..."



En buildTypes
    hacia "gradle" vamos a definir aquí entonces, que se lean estas variables, vamos
    hacer
        "buildConfigField" que es de tipo String y le vamos a poner el nombre, en este caso
        es "TWITEER_KEY", luego "Project.Properties" en singular y el nombre que va a tener es el mismo TWITTER_KEY
            buildConfigField "String", "TWITTER_KEY", project.property('TWITTER_KEY')

    entonces esto me va agregar a "BuildConfig" un valor constante con lo que sea que yo puse
    en este archivo, similar lo vamos hacer para "TWITTER_SECRET"
            buildConfigField "String", "TWITTER_SECRET", project.property('TWITTER_SECRET')



    esto lo vamos hacer
    tanto para "release" como para "debug" vamos a trabajar en este momento con "debug"
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            //definimos que se lean las variables del gradle.properties
            buildConfigField "String", "TWITTER_KEY", project.property('TWITTER_KEY')
            buildConfigField "String", "TWITTER_SECRET", project.property('TWITTER_SECRET')
        }

        debug {
            buildConfigField "String", "TWITTER_KEY", project.property('TWITTER_KEY')
            buildConfigField "String", "TWITTER_SECRET", project.property('TWITTER_SECRET')
        }





    Por ultimo vamos a colocar las dependencias necesarias para
    las librerías
            "soporte" del SDK de Twitter para loguin y para obtener los resultados,
            "recyclerView" porque así voy a mostrar mi contenido
            "AppCompat"
            "cardView" porque cada elemento del"recicleView" va ser un "cardview"
            "support"
            "design" para elementos de "Materials",
            luego esos tres son para "dagger" la librería como tal,
            la librería de anotaciones y el compilador
            de "dagger" necesario para la inyección de dependencias,
            "butterknife"
            "glide"
            "event"



    Para poder usar la configuración de Twitter y que este se loguee con nuestras credenciales procedemos a realizar lo siguiente

    para eso necesitamos editar  "style.xml" y vamos agregar algo que quite
    la barra para usar un "toolBar" entonces por aquí colocamos lo necesario, como ahora tenemos
    un "Theme.NoActionBar" podemos volver al "manifest" e indicarle aquí que va tener un "Theme.NoActionBar"
    de tal forma que en toda la aplicación no va ver un "ActionBar" y nosotros vamos a configurar
    el "toolBar" como lo consideremos necesario,

         <!--Agregamos algo que quite la barra para colocar un Toolbar-->
            <style name="AppTheme.NoActionBar">
                <item name="windowActionBar">false</item>
                <item name="windowNoTitle">true</item>
            </style>
            <style name="AppTheme.AppBarOverlay" parent="ThemeOverlay.AppCompat.Dark.ActionBar" />
            <style name="AppTheme.PopupOverlay" parent="ThemeOverlay.AppCompat.Light" />


    Continuamos

    Para continuar a crear un "AplicationClass",  esto va ser un "TwitterClientApp" y va heredar de "Application" recordamos que esta clase
    hay que especificarla también en el "manifest" con un "AndroidName" para que funcione
                    android:name=".TwitterClientApp"


    y aquí vamos a implementar algunos métodos, entonces vamos a sobrecargar el método "OnCreate"
    que está por aquí y llamamos a "initTwitter" creamos este método aquí lo que vamos hacer
    es un "TwitterAuthConfig"igual "NewTwitterAuthConfig" a partir de "(Build.Config.TWITTER_KEY.BuildConfig.TWITTER_SECRET)
    aquí estamos leyendo las variables que definimos en "gradle" y hacemos "Twitter.initialize(this)"
    para indicar al "aplicationClass" y un "new Twitter" a partir de la configuración que
    tenemos, compilamos y estamos listos, tomemeos en cuenta que esta configuracion la tenemos desde la documentación oficial de twitter
                @Override
                    public void onCreate() {
                        super.onCreate();
                        initTwitter();
                    }

                    private void initTwitter() {
                        TwitterConfig config = new TwitterConfig.Builder(this)
                                .logger(new DefaultLogger(Log.DEBUG))
                                .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET))
                                .debug(true)
                                .build();
                        Twitter.initialize(config);
                    }





Commit2

    Recordemos que nos interesa acceder al home timeline del usuario a través del "statuses/home_timeline"
    esto nos va devolver los tweets más recientes publicados por el usuario autenticado y los
    usuarios a los que este usuario les ha dado follow,

    pero eso no viene por defecto soportado por "Twitter", entonces va ser necesario extender el API y está pensado
    para que lo podamos ver de una manera muy sencilla, está usando "retrofit" que es una
    librería pensada para peticiones de APIS,


    requiere extensión un servicio y un cliente, tal como lo soporta
    "retrofit" entonces el servicio, vamos a pedirlo directamente a hacia "statuses/home_timeline"
    con alguna de las variables que hemos visto como
        "count"
        "trim_user"
        "exclude_replies"
        etcétera


    Podemos observar mas sobre este tema en https://developer.twitter.com/en/docs/tweets/timelines/api-reference/get-statuses-home_timeline.html




    Además el cliente va extender de un "TwitterApliClient" de esta forma puedo
    inicializar las peticiones que eventualmente pueda hacer,

    vamos a crear un paquete nuevo y este le llamamos "API", dentro del mismo vamos a crear unas clases, de hecho la primera
    es una interfaz, esta interfaz, le vamos a llamar "timelineservice" y aquí vamos a especificar
    cuales el "endpoint" que queremos,
    nos interesa una petición "get" hacia "1.1/statuses/home_time_line.json"
    y este es por petición la vamos hacer por un método "homeTimeLine" que va recibir varios
    parámetros y uno de los parámetros va ser un "callback" a ejecutar cuando termine, entonces
    los parámetros incluyen
    un "count" a este lo vamos a llamar "count" de tipo "integer"
    también "trim_user" estos parámetros están especificados en el API de Twitter y los especificamos en mas arriba,
    "include_entitles"
    el "Query" de quien contribuye, que incluya las entidades que nos va servir para obtener imágenes y hashtags
    por ultimo "callback" este "callback" va ser sobre un listado de tweets, estos tweets no son tweets que yo estoy definiendo sino es de lo que me provee
    "twitter" entonces le vamos a poner a este nombre "callback" y estamos listos con el
    servicio,

    Interfaz TimeLineService
            @GET("/1.1/statuses/home_timeline.json")
            void homeTimeline(@Query("count") Integer count,
                              @Query("trim_user") Boolean trim_user,
                              @Query("exclude_replies") Boolean exclude_replies,
                              @Query("contributor_details") Boolean contributor_details,
                              @Query("include_entities") Boolean include_entities,
                              Callback<List<Tweet>> callback);


    Debemos pasar a crear también un cliente, este le vamos a llamar "customTwitterApiClient"
    y va heredar de "TwitterApiClient" lo cual me forza a tener un constructor con la "session"
    y vamos a darle también un "get" "TimeLineService getTimeLineServices" entonces a través de
    la interfaz, voy a exponer cual es vamos "getService(TimeLineService.class)"

    'a través del interfaz voy a exponer cual es la forma de acceder a este API y a través del cliente voy a poder obtener esa interfaz todo lo demás lo va implementar Twitter',
    entonces no nos debemos preocupar mucho en la sesión, etcétera, sino simplemente del "endpoint",
    es decir no nos interesa el BaseUrl de Retrofit ya que en esata clase le especificamos el servicio
    y twitter tiene ya embebido cual es el link y configurado , solo nos preocupamos como ya dije del "endpoint" (URL a realizar un GET)


            public class CustomTwitterApiClient extends TwitterApiClient {

                public CustomTwitterApiClient(TwitterSession session) {
                    super(session);
                }

                public TimeLineService getTimeLineService(){
                    return getService(TimeLineService.class);
                }
            }

