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





Commit2 ":2EndConfigurationAPI"

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


Commit3 :Librerias(EventBus-Glide)
    Librerías


    Seguimos con el desarrollo pero en este caso vamos a crear el árbol del proyecto vamos a trabajar con diferentes librerías

    Creamos un paquete para las librerías y como habíamos hecho con el Chat teníamos hecho envoltorios de estas librerías,
    estos envoltorios especificaban, una serie de clases "base", en este caso yo he definido dos aquí, con cosas básicas que es lo que voy a usar de la librería


        lib
            base(interfaz)
                "eventoBus"
                "imageLoader"



    luego las implementaciones específicas que íbamos a usar por ejemplo comenzamos para EventBus
            "GreenRobotEventBus" y va implementar de"eventBus", el EventBus que yo generé
            "GlideImageLoader" y va implementar de "ImageLoader"

        lib

           GreenRobotEventBus
           ImageLoader

           base
               ...


    Entonces voy a tener un "org.GreenRobot.EventBus eventBus" y sobre este voy a trabajar entonces
        "eventBus.register(subscriber)"
        "eventBus.unregister(subscriber)" y
        "eventBus.post" del evento

    Lo que había hecho hasta el momento, era tener un "Singleton" en vez de eso vamos a colocar un constructor que me va permitir acceder,
    lo voy a recibir de aquí, y lo voy a inyectar cuando lo necesite la clase y de esa forma voy a poder proveer esa clase, para cuando lo utilicen la clases
    que voy a implementar eventualmente

     Antes para tener un Singleton
                private static class SingletonHolder{
                    private  static  final GreenRobotEventBus INSTANCE= new GreenRobotEventBus();
                }

                public static  GreenRobotEventBus getInstance (){
                    return SingletonHolder.INSTANCE;
                }

                public GreenRobotEventBus() {
                    this.eventBus = org.greenrobot.eventbus.EventBus.getDefault();
                }

    Ahora con Inyección de Dependencias, ya que luego los configuro queda así

                org.greenrobot.eventbus.EventBus eventBus;

                public GreenRobotEventBus(org.greenrobot.eventbus.EventBus eventBus) {
                    this.eventBus = eventBus;
                }



    Ahora sí, y de la misma manera voy hacer un "GlideImageLoader" que
    implemente un "ImageLoader" eso me forza a implementar el
             vamos a agregar un "private RequestManager glideRequestManager"

             Método de "load",dentro del cual vamos hacer "glideRequestManager.Load" ponemos el "URL"
             vamos hacer "diskCacheStrategy(DiskCacheStratefy.ALL)" esto es para que los cachea información original y datos transformados
             luego centerCrop, para que vaya centrado
             vamos hacer un "override" del tamaño para que las imágenes siempre sean de ese tamaño
             que lo cargue dentro del "imageView" y listo,


         No hemos encontrado estos elementos directo por lo que tuvimos que implementar RequestOptions para poner las opciones y luego cargarla quedando de la siguiente manera
         En GlideImageLoader
                 public void load(ImageView imgAvatar, String url) {
                        RequestOptions requestOptions = new RequestOptions();
                            requestOptions
                                    .centerCrop()
                                    .override(600, 400)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                        glideRequestManager
                                .load(url)
                                .apply(requestOptions)
                                .into(imgAvatar);

                }


         Ahora este "glideRequestManager" debería estar definido por Glide.whit(context) pero lo veremos mas adelante

NOTA:Podemos encontrar mas información dentro de https://code.tutsplus.com/es/tutorials/code-an-image-gallery-android-app-with-glide--cms-28207


Commit4 : DependencyInjectionConfiguration

        Ahora procedemos a crear un package nuevo y le vamos a llamar "DI" de "Dependence Injection" y voy a implementar aquí lo necesario para la inyección de dependencias,

        La dependencias con "dagger 2" va necesitar dos clases, de hecho una es una interfaz, es decir una clase y una interfaz

        En la clase, es la que provee la inyección como tal
        En la interfaz, se va a definir el "API" de cómo inyectar, ,


        Definimos primero la clase para este ejemplo
        a esta le vamos a llamar "LibsModule" es un módulo y le colocamos la anotación de modulo
        que voy a necesitar proveer, voy a necesitar proveer un
                "EventBus" y un "ImageLoader" hasta el momento

        vamos a colocar un método que devuelva un "EventBus" y se llame "ProvidesEventBus" y
        este lo que tiene que recibir es una instancia de el Bus de eventos que tengo en mi constructor
        en "GreenRobotEventBus" y en base a esto va ser un "return new GreenRobotEventBus" usando el parámetro recibido,
        voy agregarle una anotación aquí, que diga que es un "Singleton" es decir que solo va a ver una instancia y que es un método
        que provee, debemos tomar en cuenta que debemos devolver el EventBus que nosotros hemos definido,



        Necesito definir de donde va venir este "EventBus", entonces necesito hacer otro
        método que devuelva una instancia de este tipo le vamos a llamar " providesLibraryEventBus"
        y esto no va recibir absolutamente nada, y lo que va devolver es, una instancia a partir
        de "eventBus.getDefault" veamos "eventBus.EventBus"

         En síntesis de la librería pasa a mi objeto que eventualmente devuelve este genérico,algo cascada

            @Provides
                @Singleton
                EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus) {
                    return new GreenRobotEventBus(eventBus);
                }

                @Provides
                @Singleton
                org.greenrobot.eventbus.EventBus providesLibraryEventBus() {
                    return  org.greenrobot.eventbus.EventBus.getDefault();
                }

        Vamos hacer algo similar, para tener, un "providesImageLoader" entonces vamos
        a colocarle aquí "providesImageLoader" y voy a necesitar instanciar a "GlideImageLoader"
        que recibe un "RequestManager"
        entonces vamos a recibir como parámetro un "RequestManager", entonces tengo que hacer otro "provides" específico
        para esto, que va a devolver un "RequestManager" le llamamos "providesRequestManager" esto
        no va a recibir absolutamente nada de parámetros y vamos a devolver aquí un "Glide.with" y
        allí donde necesito un contexto, entonces tengo varias opciones, podría ser que recibo
        un contexto que inyecte un contexto así como estoy haciendo un módulo para las librerías,
        podría ser un módulo para la actividad, la aplicación completa del "Twitter Client App" en este caso lo que voy a hacer es definir aquí un fragmento "private Fragment Fragment"
        y en el constructor lo voy a recibir, entonces lo que va a recibir este "providesRequestManager"
        para este caso puntual va ser un fragmento, entonces hacemos un "Glide.whith" con ese
        fragmento y  de la misma forma voy a tener un "providesFragment" para proveérselo al "RequestManager"
        y lo que vamos a devolver es un fragmento que ha recibido este módulo
        y lo que voy a devolver es el fragmento que recibió este módulo,
        entonces si se dan cuenta como una cascada a través de la cual voy proveyendo dependencias
        lo que esto me permite, ya termino la construcción ya está compilado a veces me da un problema
        al compilar cuando estoy modificando estas clases entonces tengo que compilar dos veces,
        para que queden sin ningún problema, recuerden "dagger" está generando en tiempo de compilación
        varias anotaciones y entonces compilar algunas veces no encuentra esas clases necesarias cuando quiere instanciar
        el modulo, en esta caso ya está compilado,

            @Provides
            @Singleton
            ImageLoader providesImageLoader(RequestManager requestManager) {
                return new GlideImageLoader(requestManager);
            }

            @Provides
            @Singleton
            RequestManager providesRequestManager(Fragment fragment) {
                return Glide.with(fragment);
            }


            @Provides
            @Singleton
            Fragment providesFragment() {
                return this.fragment;
            }


        de esta forma estoy proveyendo las dependencias
        de las librerías, si yo fuera usar un "GlideImagenLoader" o un "org.greenrobotEventBus" en alguna clase,
        tendría que especificar un "LibsComponent" así como tengo este "LibsModule" y allí
        especifico como se va a inyectar, ya sea con un método genérico "inject" especificando
        cual es la clase que lo va usar, usualmente un "Activity" o "Fragment" o bien con métodos
        que dicen como obtengo alguno de estos elementos que estoy proveyendo, por ejemplo podría
        tener un "getImageLoader" sin embargo no voy a hacerlo tal cual, sino voy a hacerlo a través
        de otro módulo de inyección, entonces no voy a escribir un componente en este momento,
        sino que cuando escriba el modulo componente correspondiente, alguno de los "Feature" o
        características que voy a estar trabajando, voy a vincular con este módulo que ya existe,
        por el momento dejamos hasta allí el módulo de librerías con sus respectivos inyección
        de dependencias.


Commit5: :TwitterLoginDocumentation


    Como vemos aún no tenemos nada en nuestro login, y vamos agregar aquí una referencia
    a un "widget" de Twitter, para el layout` eso está en "com.twitter.sdk.android.core.identity.TwitterLoginButton"
    este elemento tiene que tener por lo menos, ancho y alto vamos a usar "wrap_content" vamos a centrar
    y por ultimo le ponemos un identificador, este identificador nos va servir para obtenerlo
    en la clase por el momento, es posible que algún error y lo quiero reportar aquí, le vamos a agregar un identificador
     al "container" le vamos a llamar "container", este nos sirve para el snackbar

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                        android:id="@+id/container"
            ...
                <com.twitter.sdk.android.core.identity.TwitterLoginButton
                        android:id="@+id/twitterLoginButton"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_centerInParent="true"
                        />

    Ahora procedemos aquí hacer la inyección de vistas con "bufferKnife" me interesan las únicas dos vistas que tengo
    el botón y el "relativeLayout", y vamos a especificar al botón se llama "TwitterLoginButton.setCallBack" este "CallBack" lo vamos a hacer en base a
    la sesión y solo tengo dos resultados, éxito o fracaso,
     En éxito vamos a hacer "navigateToMainScreen" voy a necesitar una actividad "main" eventualmente y si fallo,
     vamos a reportar un error de porque fallo, con el snackbar
    En las inyecciones de Butterknife tengo un problema al hacer el render del botón de Twitter por lo que la instancia me ha tocado hacer de forma manual


        twitterLoginButton=(TwitterLoginButton)findViewById(R.id.twitterLoginButton);



                twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        navigateToMainScreen();
                    }

                    @Override
                    public void failure(TwitterException exception) {

                        ....Error
                    }
                });



    vamos a crear este método "navigateToMainScreen"
    me va a cambiar de actividad entonces a crear de una vez un nuevo paquete que le llamamos
    "main" y dentro de este paquete "main" vamos a crear una nueva actividad nueva vacía,


    Volvemos aquí al "loginActivity" aquí lo que vamos a hacer es un "StartActivity (new Intent(this, MaingActivity.class))"

        private void navigateToMainScreen() {
            startActivity(new Intent(this, MainActivity.class));
        }

    Necesitamos también sobrecargar con "onActivityResult"
    para cuando nos regrese el SDK de Twitter hacia nuestra actividad y le vamos a poner
    aquí al botón, "twitterLoginButton.onActivityResult()" y le enviamos los mismos parámetros "resultCode"
    "data" esta forma que el mismo botón va ejecutar esto y si tengo éxito me va a llevar al "MainActivity"

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            twitterLoginButton.onActivityResult(requestCode,resultCode,data);
        }


    En caso que no pueda tener un buen inicio de sesión vamos a mostrar un error, este error debería tener un poco de detalle y
    tenemos un "twitterException" de que podemos obtener algo, pero vamos a agregar un mensaje
    en "String.xml" en este mensaje vamos a especificar que no se pudo iniciar la sesión y que tengo
    un string adicional para especificar lo que sea que recibí de parte de "Twitter".

    Entonces aquí vamos a mostrarlo con un "Snackbar" la forma de hacerlo es "msgError = String.format(getString)"
    y le envió este "String" que acabo de agregar y además le envió "e" de la excepción "e.getLocalizerMessage"
    para que me dé algo en base a la localización del usuario me refiero a idioma etcétera
    y luego hago un "SnackBar.make" necesito enviar una vista para eso me va servir el "container"
    y luego el mensaje de error tiene duración corta y lo mostramos,

                String msgError = String.format(getString(R.string.login_error_message), exception.getMessage());
                 Snackbar.make(container, msgError, Snackbar.LENGTH_SHORT).show();


    Recuerden yo ya tengo mi aplicación configurada y en este caso no tengo el cliente de Twitter
    instalado, entonces me va pedir que inicie sesión aquí, vamos a iniciar sesión, autorizamos
    en API, en este caso ya había autorizado el APP porque soy el desarrollador y listo,
    allí me dirigió a la nueva actividad.
    Sin embargo si cerramos la aplicación,
    volvemos a abrirla aquí está el "TwitterClient" me vuelve a
    pedir login, esto es algo incómodo, entonces vamos a corregir aquí que si ya existe una
    sesión la podamos reutilizar, escribimos

    "if(TwitterCore.getInstance().getSessionManager().getActiveSession() = Null)" es diferente que null entonces
    que me lleve a la pantalla principal y volvemos a ejecutar, vamos a decirle que use el mismo emulador para la siguiente vez, está listo
    y ahora ya tengo la sesión iniciada

                if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                                    navigateToMainScreen();
                    }


    NOTA: Toda esta parte la hemos sacado de la documentación oficial de Twitter
        https://dev.twitter.com/twitterkit/android/log-in-with-twitter



Commit6 :layoutActivityMain

    Nuestra aplicación va tener una visualización en este formato:
            con un encabezado,
            el nombre del APP,
            un menú donde vamos a poder cerrar sesión
            tabs o pestañas una de ellas va mostrar contenido de imágenes y la otra de "Hastags", voy a poder cambiar a través de
            hacer clic en el encabezado, donde están los nombres de los tabs o a través de un "drag"

            eso quiere decir que voy a usar un "ViewPager" voy a tener una actividad principal "MainActivity" que va ser el "Host" y ambos, tanto imágenes como Hashtags van a ser fragmentos
            que van a presentar el contenido específico

    Comenzamos editando el layout

    esta actividad, vamos a empezar agregando unos "strings" para esta actividad

            <string name="main.header.images">Imágenes</string>
            <string name="main.header.hashtags">Hashtags</string>
            <string name="main.menu.action.logout">Cerrar sesión</string>


    vamos a ir a "activityMain" para modificar el contenido
    Colocamos un "coordinatorLayout" en ves de  un RelativeLayout le doy su respectivo contexto respectivo




    entonces voy a tener un "coordinatorLayout"
    que lo va ser es coordinar los elementos dentro de él,
    allí adentro un "appbarLayout"
        adentro un "toolbar"
        este "toolbar" va tener la parte del menú, el nombre de la aplicación incluso que el usuario inicio sesión
    los tabs
    adentro del contenido principal
        vamos a tener un "viewPaper"


En activity_main.xml
    <android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/main_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true"
        tools:context=".main.MainActivity">

        <android.support.design.widget.AppBarLayout
            android:id="@+id/appbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:paddingTop="@dimen/appbar_padding_top"
            android:theme="@style/AppTheme.AppBarOverlay">

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:background="?attr/colorPrimary"
                app:layout_scrollFlags="scroll|enterAlways"
                app:popupTheme="@style/AppTheme.PopupOverlay">

            </android.support.v7.widget.Toolbar>

            <android.support.design.widget.TabLayout
                android:id="@+id/tabs"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />

        </android.support.design.widget.AppBarLayout>

        <android.support.v4.view.ViewPager
            android:id="@+id/container"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior" />



    adicional a esto vamos a crear un
    archivo aquí mismo y le vamos a llamar a este "fragment_content" para especificar que
    va ser el "layout" de un fragmento pero en los dos casos vamos a mostrar contenido similar,
    en los dos casos van a ser listados, entonces vamos a editarlo de tal forma que podamos
    reutilizarlo para las dos vistas de nuestra aplicación


    lo que tenemos aquí entonces es un Framelayout para que se vean los elementos encima de otros
    dentro
        vamos a poner un "android.support.v7.widget."
            le vamos agregar también que tenga un ancho y alto
            en este ancho y alto le vamos a poner que el ancho ocupe todo, pero del alto únicamente lo necesario
            por supuesto tiene que llevar un identificador este identificador va ser "RecyclerView"

        además vamos poner un "progressBar"
            tamaño grande y centrado
            lo vamos a dejar solo centrado, para que este solo horizontal y verticalmente,
        entonces esta mi listado y encima está el "progressBar" solamente uno de los dos se va ver a la vez su "progressBar" le vamos
        a poner "visibility =gone""


    fragment_content
    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        ...
        >
        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

        <ProgressBar
            style="?android:attr/progressBarStyleLarge"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/progressBar"
            android:layout_gravity="center"
            android:visibility="gone" />
    </FrameLayout>

    tomemetos en cuenta que cuando creamos un Framelayout debemos quitra la orientación (Vertical-Horizontal)

Commit7 :LayoutImplementation

    Vamos a agregar un menú para el cierre de sesión,
    Primero editamos el "Layout" para esto creamos un nuevo directorio y a este le llamamos menú y bajo este menú,
        vamos a crear un nuevo recurso de menú, que le vamos a llamar "menu_mail.xlm"
        aquí vamos agregar un "item" ese "item" va ir asociado a la actividad

        Para ver como funciona un menu y las propiedades podemos ver https://developer.android.com/guide/topics/ui/menus.html?hl=es-419 y verificar que no mas hace cada configuración

        <menu xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            xmlns:tools="http://schemas.android.com/tools"
            tools:context=".main.ui.MainActivity">
            <item
                android:id="@+id/action_logout"
                android:orderInCategory="100"
                android:title="@string/main.menu.action.logout"
                app:showAsAction="never" />
        </menu>



    Creamos un paquete main y en este damos nuestra lógica
        main
            ui
              ...Activity, Fragments, Views

            adapters
              ...Images, adaptadores para recyclerView


    Desde el "main activity" vamos a trabajar,
    entonces necesito sobrecargar dos métodos aquí,

            vamos a sobrecargar "onCreateOptionMenu"
                inflamos aquí "getMenuInflater().inflater(R.menu.menu_main

            vamos a "onItemSelected"
                Aquí vamos a verificar si acaso el "if(item.getIdemId() == R.id.action_logout)"

            entonces hacemos una llamada a "logout" este método aún no existe, lo vamos a crear y
            hacemos un "TwitterCore.getInstance().clearAuth"

            Además de eso vamos a llevar al usuario hacia la pantalla principal con un "intent" "intent=new Intent(this, LoginActivity.class)" pero le vamos agregar
            un par de banderas para que el usuario
                    no pueda al darle "back" cuando está en la pantalla
                    del "login" volver a esta pantalla, por ultimo iniciamos la actividad y listo, con esto tenemos
                    el "logout" necesito agregar el "toolBar" para hacer "Sign Up" y todo lo que tengo que hacer
                    aquí es "setSupportActionBar(toolbar)" volver a ejecutar y vamos a ver como quedo ahora,
                    ahora si tengo un "logout" entonces con eso puedo cerrar sesión, cuando este aquí con
                    la sesión iniciada y le doy "back"

                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                            Intent intent = new Intent(this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


            Tenemos un problema similar a las banderas en "loginActivity" por lo que procedemos agregarlos aquí
            volvemos a ejecutar entonces ahora que le dé "Back" de "loginActivity" no me va a regresar a "Main"
            y de "Main" cuando le dé "Back" no me va a regresar a "login" entonces estoy aquí
            en la pantalla principal, puedo cerrar sesión le doy "Back" todavía no me regresa "login"
            abro el cliente otra vez, le doy cerrar sesión me lleva "LoginActivity" le doy "Back" y tampoco
            me regresa al contenido principal,


Commit8:CreateViewPagerFunctionality

    En este apartado vamos a definir como trabajar con un ViewPager, definiendo los posibles Fragments y su respectiva funcionalidad

    Adaptador para Tabs

    Vamos a empezar con los tabs haciendo un adaptador, este adaptador nos va a permitir manejar el
    contenido, se puede manejar cuando no son fragmentos, sin embargo es común que las
    secciones, sean fragmentos. vamos hacer un "MainSectionsPagerAdapter" Class y este va heredar
    de "FragmentPagerAdapter" por lo tanto necesito que me cargue algunos métodos


    public class MainSectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] titles;
        private Fragment[] fragments;

        public MainSectionsPagerAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
            super(fm);
            this.titles = titles;
            this.fragments = fragments;
        }

        ...
        //
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return this.fragments.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


    y lo que voy a tener es una serie de fragmentos un arreglo de fragmentos y además voy a tener un arreglo
    de títulos para ponerle nombres a lo necesario, esto lo voy a recibir en el constructor y
    antes de eso vamos a devolver el "getCount" "return this.fragments.length" ambos debería
    coincidir en tamaño y para el "items" pueda devolver "this.fragments.position" vamos agregar
    un constructor para que sirva a ambos, tiene un "fragmentManager" asignamos y listo, con
    esto está nuestro adaptador ahora podemos ir a "MainActivity" y ver la implementación
    necesaria aquí,

    En Main Activity

    Vamos al método "onCreate" aquí vamos a agregar un "setupAdapter" método dentro del onCreate creamos
    el método entonces aquí hacemos una instancia de  "MainSectionsPagerAdapter" "Adapter" enviándole como fragmentManager
    el "getSupportFragmentManager" pero además tengo que enviarle los fragmentos
    le hacemos un "new Fragment Arrays" y aquí voy a enviar los dos fragmentos que uso para mostrar en los tabs
    Luego hacemos un "String titles" "new String" y aquí voy a obtener "R.String.main_header_images"
    y "R.string.main_header_hashtags" que son los items definidos para los títulos de Las Pages,


     @Override
        protected void onCreate(Bundle savedInstanceState) {
            ...
            setupAdapter();
            //
        }

        private void setupAdapter() {
            Fragment[] fragments=new Fragment[]{new ImagesFragment(),new HashtagsFragment()};
            String[] titles= new String[]{getString(R.string.main_header_images),getString(R.string.main_header_hashtags)};

            MainSectionsPagerAdapter adapter=
                                    new MainSectionsPagerAdapter(getSupportFragmentManager(),
                                                                    titles,fragments);
            viewPager.setAdapter(adapter);
            tabs.setupWithViewPager(viewPager);
    }

    Debemos llamar de una maera que podamos conocer a un objeto por ejemeplo al viewPager llamamos "viewpager" para que nos quede claro, va ser también
    un contenedor pero llamémosle "viewPaper.setAdapter(adapter)" y luego con esto tenemos el "viewPager" listo,
    pero todavía no hay tabs,
     y creo un "tabs.setupWithViewPager(ViewPager)"y le envió en "viewPaper" y todo está listo,


    Packages
    Creo las respectivas carpetas para mis hashtags e Imagenes

    images
        ImagesFragment
        ...
    hashtags
        ...
        HashtagsFragments

    Instanciamos "newImagesFragment" en nuestro setupAdapter() y de una vez hagamos
    el otro "new HashtagFragment"


    EN adapter  MainSectionsPagerAdapter.class

    El título todavía no lo tengo y ahora debo colocarlo, minimizamos esto por aquí, entonces
    vamos a volver a nuestro adaptador y vamos a sobrecargar "getPageTitle(position)" de
    cierta posición y lo que vamos a devolver aquí es "this.titles(position)" ejecutamos
    de nuevo está esperando ver el título en cada uno de los tabs la selectividad

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }


Commit9 :LayoutImagesFragment
    Layout General con un cardView

    Teníamos ya listo nuestro "Layout" general en donde especificamos que iba ver un "RecyclerView"
    en el caso de las imágenes pues va ser una vista de "Grilla" con dos columnas pero dentro
    de cada uno de los elementos aún tengo que especificar este "Layout", de nuevo aquí en
    imágenes voy a tener un "ImagesView" y un "TextView" con eso voy a mostrar el contenido
    necesario
    Vamos a usar un "cardView" que la idea de esto es que se pueda mostrar en múltiples
    dispositivos con una misma identidad gráfica, si por ejemplo yo quisiera hacer esto para
    "web" o para televisión o para auto, entonces la idea de "CardView" es que mantenga esa
    identidad a lo largo de todos los dispositivos


    Vamos entonces a hacerlo a "AndroidStudio"
    que vamos a crear un "Layout" nuevo y a este lo llamamos "content_images.xml" el "Root Element" le hacemos un "Relative Layout"
    Reemplazamos todo por un "CardView", dentro de "CardView" tengo un "RelativeLayout" y adentro
    un "ImageView" y un "TextView" Teniendo un archivo de este tipo

    <android.support.v7.widget.CardView
        ...
        >

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:id="@+id/imgMedia"
                android:layout_margin="@dimen/activity_horizontal_margin"
                android:layout_alignParentTop="true"
                android:layout_alignParentLeft="true"
                android:layout_alignParentStart="true" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="@dimen/activity_horizontal_margin"
                android:id="@+id/txtTweet"
                android:layout_below="@+id/imgMedia"
                android:layout_centerHorizontal="true" />
        </RelativeLayout>
    </android.support.v7.widget.CardView>



Commit10 :ImagesFragmentMVP

    Arquitectura MVP, vamos a definir nuestra Arquitectura MVP para usarla dentro de Images

    Definimos primero todas las interfaces que definien abstracciones

        ImagesView
            //Mostrar, ocultar elementos
                void showElements();
                void hideElements();
            //Mostrar, ocultar progressBar,
                void showProgress();
                void hideProgress();
            //Cuando haya un resultado podemos tener un error
                void onError(String error);
            //podemos hacer un setContent con un listado de Imáganes
                void setContent(List<Image> items);

                Creamos Entidad de Image
                    //para mas adelante usarla cuando usemos el adaptador

        ImagesPresenter
            Va a tener algunos metodos de EventBus Comunes
                void onResume();
                void onPause();
                //para evitar el Memory lick, destruyendo la vista
                void onDestroy();
                //para traer el contenido desde la api
                void getImageTweets();
                //Creamos el IMagesEvent que son los eventos de Images y lo usamos cuando veamos el presentador
                void onEventMainThread(ImagesEvent event);


        ImagesInteractor
            Va a conectar
                //método que permite ir a traer el contenido
                void execute();


        ImagesRepository
            Voy a tener un método que llama
            void getImages();


    Con esto tenemos definido los contratos que van a implementar las clases, las clases concretas, la que tienen los detalles de implementación
    se van a basar en estas abstracciones para presentarme los resultados de este Feature que va a traer imágenes


Commit11 :ImagesAdapter
    Procedemos a realizar un adaptador del recyclerView de nuestra vista

    En Entity Images procedemos a crear sus atributos

    necesitamos una forma de identificarlas,
    necesitamos también cual es el URL para ir a traerlas con "Glide" y mostrarlas
    necesitamos saber cuál es el texto asociado
    necesitamos saber cuál es la cantidad de conteo de favoritos porque en base a esto vamos a ordenar
    necesitamso una acción que al darle click nos lleve al "tweets" entonces vamos a definir
    aquí una constante que se llame "BASE_TWEET_URL =" y lo vamos asignar hacia este "string"
    para ir a traer el "tweet"

        private String id;
        private String imageURL;
        private String tweetText;
        private int favoriteCount;


    Como nota podemos agregar un detalle con la forma en que Twitter devuelve los resultados,
    cuando queremos acceder a un tweet por su identificador el problema que tenemos es que
    nos redirige a "m.twitter" y a veces este no existe, entonces por eso estamos usando
    esta forma para redirigir hacia el contenido

        private final static String BASE_TWEET_URL = "https://twitter.com/null/status/";


    Vamos hacer "Getter" y "Setter" para todos
    y por ultimo vamos hacer un "public string getTweetUrl" y aquí hacemos un "return BASE_TWEET_URL" concatenado el "this.Id"

        public String getTweetURL() {
            return BASE_TWEET_URL + this.id;
        }


    Creamos nuevo paquete en Images para la vista

        Images
            ui
                adapters
                    ImagesAdapter


    "ImagesAdapter" aquí voy a trabajar en este momento que características tiene la clase, como funciona con un "RecyclerView"
    necesito que herede de "ReclyclerView.Adapter", defino el "ViewHolder" lo voy a definir aquí con
    un "private class Viewholder" que hereda del "RecyclerView.Viewholder" por tanto tiene
    que tener un constructor y por lo tanto vamos a implementar los métodos necesarios le puedo
    especificar aquí que tipo de "holder" va usar y quiero que use un "Viewholder" "Viewholder"
    de mi clase, "imagesAdapter"

        Pasos para crear el adaptador, con su Interfaz de Click siempre debemos seguir estos pasos (Contexto Generalizado)


            public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
                ///...
                public class ViewHolder extends RecyclerView.ViewHolder {
                //....
                }
                ...
            }

            Crear Clase que herede de ReclyclerView.Adapter
                Implementa una clase estática que esta dentro de este, con el mismo nombre <NombreAdaptador.ViewHolder>
            Se crea la clase ViewHolder y ademas hereda como su clase contenedora, RecyclerView.ViewHolder
                Se crea el construcotr que necesitamos con la vista asociada a este parámetro con los datos creados como pertinentes (privates)
                    Todo esto lo vamos a recibir en el constructor eventualmente aquí voy a usar la inyección de dependencias pero
                    por el momento solo asumo que viene en el constructor
            Creamos la interfaz para el manejo del click OnItemClickListener que recibirá nuestro Objeto a ser pasado durante el click del tweet

            Creo la forma en la que quiero guardar mis elementos (Arrays, Dataset,HashMaps, etc)
            En este caso he usado también un imageLoader por loque me ayudará Glide pero puede usar otra librería para imagenes, como Picasso entre otras


            Dentro de los métodos
                onCreateViewHolder
                    hacer un "View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_images" y le mando "parent" y luego falso
                    lo que va devolver es un "New ViewHolder(view)" en base a este "view"

                    el cual me ayudará a inflar mi vista dentro de el cardView, pero en este podemos optar por poner la actividad(xml) o un fragment(xml)

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_images, parent, false);
                        return new ViewHolder(view);
                    }

                getItemCount
                    defino mi "dataset.size"

                       @Override
                        public int getItemCount() {
                            return dataset.size();
                        }
                Dentro de ViewHolder Class


                                    //inyecciones de atributos del contenedor en este caso el CardView, puede ser Fragment(xml), u actividad(xml)
                                    private View view;

                                    public ViewHolder(View itemView) {
                                        super(itemView);
                                        ButterKnife.bind(this,itemView);
                                        this.view=itemView;
                                    }

                                    public void setOnclickListener(final Image image, final OnItemClickListener listener){
                                        view.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                listener.onItemClick(image);
                                            }
                                        });
                                    }

                Aquí vamos a definir un "View" y lo vamos asignar dentro del constructor
                    vamos hacer también un "ButterKnife.bind(this,itemView)"

                Aquí voy a necesitar un método sea "public void setOnClickListener"
                que va recibir la imagen actual y el "onClickListener" y lo que voy hacer es colocarle aquí a "View.setOnClickListener(new View.OnClickListener)"
                aquí digo "Listener.onItemClick" le envió la imagen, listo me hace falta que
                especificar mi contenido que tengo en el "ViewHolder" tengo dos elementos el la imagen y el "TextView"



    Ahora me hace falta implementar un método  para cuando reciba los "items" vamos a poner aquí un "private void setItems(List)" que
    va recibir un listado "items" lo que voy hacer es a lo que ya tengo le voy a agregar estos
    nuevos "items" y voy a notificar de un "DataSetChanged"

                public void setItems(List<Image> newItems) {
                        dataset.addAll(newItems);
                        notifyDataSetChanged();
                    }



    Luego un "onBindViewHolder" recordemos que este funciona como conector con la vista
    a traer "imageTweet" a partir de "dataset.get(position)" y luego voy a colocarle los valores entonces
    necesito colocarle el "Listener" necesito colocar el texto "imageTweet" seria ".getTweetText"
    necesito un "imagenloader.load()" le tengo que
    enviar un "imageView" en este caso es "holder.imgMedia," y le tengo que enviar un "URL" que está en
    "imageTweet.getImageURL"

            Image tweet = dataset.get(position);
            holder.setOnclickListener(tweet, clickListener);
            holder.txtTweet.setText(tweet.getTweetText());
            imageLoader.load(holder.imgMedia, tweet.getImageURL());

Commit12 :ImagesFragmentView

    Fragment y View
        (ImagesFragment)
    Vamos a trabajar con el fragmento "ImagesFragment" este fragmento tiene que implementar a "ImagesView" y "onItemClickListener"
    para el adaptador ambos implican que necesitamos métodos adicionales con "CreateView" actualmente
    solo esta renderizando mostrando una vista,
            public class ImagesFragment extends Fragment implements ImagesView, OnItemClickListener {

    Para usar una vista necesaria y poderla usar dentro de nuestra Butterknife1 por lo que
    vamos hacer un "View view =Inflater. Inflater(R.layout.content_images, container, false)" y sobre esta vista vamos hacer un "void ButterKnife"

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_content, container, false);
                ButterKnife.bind(this, view);
                return view;
            }
    Aquí uso un "Progressbar" y un "RecyclerView", que los defino como Butterknife.



    Tengo que implementar muchos métodos que quedaron esperando para que lo haga,
    Voy a necesitar un presentador, entonces definimos aquí un "ImagesPresenter" vamos a eventualmente
    a inyectar necesito aquí un "images adapter" también vamos a inyectarlo pero en base a
    esto voy a trabajar,

                ImagesPresenter presenter;
                ImagesAdapter adapter;

    Vamos primero con varios métodos del presentador, como acotación importante en este apartado podemos observar que onDestroy y onPause son llamados antes de la llamada a su ancestro

                @Override
                public void onResume() {
                    super.onResume();
                    presenter.onResume();
                }

                @Override
                public void onPause() {
                    presenter.onPause();
                    super.onPause();
                }

                @Override
                public void onDestroy() {
                    presenter.onDestroy();
                    super.onDestroy();
                }


    Listo me tocaría ver "ShowElements" "hideElements"
    entonces vamos a cambiar la visibilidad del "RecyclerView" aquí "recyclerView.setVisibility(View.VISIBLE)"
    o "View.GONE" aquí vamos hacer lo de "ProgressBar" "progressBar.setVisibility(View.VISIBLE)"
    "GONE", este "ShowElement"  también se lo puede llamar como "showImages"

    Tengo el método "setItems" dentro del adaptador y lo puedo llamar desde aquí (ImagesFragment), "adapter.setItems(items)"

        @Override
        public void setContent(List<Image> items) {
            adapter.setItems(items);
        }

    Cuando hay un error nos va hacer un "Snackbar.make" la vista que voy a usar es el contenedor,tomemos en cuenta que acá podemos usar el nombre del contenedor que engloba todo, (Framelayout,LinearLayout,RelativeLayout, ...)
    Llamamos al "container" y ahora ya lo puedo usar en el "SnackBar" "container, " el error
    que quiero mostrar en este caso lo voy a enviar tal cual, luego la duración es duración
    corta y lo muestro con "show"

        @Override
        public void onError(String error) {
            Snackbar.make(container,error,Snackbar.LENGTH_SHORT).show();
        }

    Nos falta manejar el método del Click del "items" aquí lo que vamos hacer es obtener el "URL" entonces vamos hacer un "Intent intent = new intent " y le vamos a enviar "Intent.ACTION_VIEW, Url.parse" y lo que le envío aquí es la
    imagen "image.getTweetUrl" entonces empezamos una actividad nueva a partir de este

        @Override
        public void onItemClick(Image tweet) {
            Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(tweet.getTweetURL()));
            startActivity(i);
        }

    Ahora me hace falta implementar el resto de mi "stack" es decir el presentador y el interacturador
    dentro del repositorio previo a probarlo ,en este caso como vamos hacer la inyección entonces
    voy a implementar todo esto que no son métodos muy largos y luego voy a implementar la inyección
    y luego lo voy a probar.


Commit13 :PresenterEInteractorImages

    EN ImagesEvent

    Vamos implementar el "imagesEvent" que es el que usaremos para EventBus,
    para mi evento yo necesito dos cosas,

        1.-Un error por si acaso ocurrió, podría ser "null"
        2.-Un listado de imágenes que obtuve del "API"
        Hacemos getters y setters a los dos

            private String error;
            private List<Image> images;

    Aquí hay varias cosas que podemos implementar por ser una clase de EventBus por lo que usaremos
    la documentación que voy a poner a continuación, ya que tiene buenos ejemplos de como usarlo y para que usarlo, por ejemplo como un LocalBroadcastRecebier

        http://katade.com/2016/03/15/eventbus-gestor-de-eventos-para-android/
        http://blog.intive-fdv.com.ar/eventbus-una-alternativa-al-broadcastreceiver/


    Seguimos...
    En la vista voy a necesitar un presentador que voy a eventualmente inyectar,
    pero por el momento vamos a crear un "imagesPresenterImplementation" y esta va ser una clase que implementa a "ImagesPresenter"
    por lo tanto estoy obligado a incluir algunos de esos métodos,(Siguiendo con el stack que hemos creado) que voy a necesitar aquí,

        Voy a necesitar un "eventBus" para detectar los eventos, para registrarme y deregistrarme
        Voy a necesitar la clase también "ImagesView" que es el contrato de la vista la los métodos que voy a implementar eventualmente el fragmento
        Un "ImagesInteractor"


                    private EventBus eventBus;
                    private ImagesView view;
                    private ImagesInteractor interactor;

        Luego en el constructor lo recibo todos, que vamos hacer en estos métodos,

                      public ImagesPresenterImpl(EventBus eventBus, ImagesView view, ImagesInteractor interactor) {
                        this.eventBus = eventBus;
                        this.view = view;
                        this.interactor = interactor;
                    }


    En onResume al resumir nos vamos a registrar, vamos a registrar el presentador
    con un "subcriber"

                    @Override
                    public void onResume() {
                        eventBus.register(this);
                    }

    En un "onPause" vamos a deregistrar

                    @Override
                    public void onPause() {
                        eventBus.unregister(this);
                    }

    a la hora que se destruya entonces
    la vista se vuelve nulo

                    @Override
                    public void onDestroy() {
                        view=null;
                    }

    En "getImageTweets" vamos hacer es vamos a revisar si acaso la
    vista es diferente de "null" entonces llamamos un par de métodos de la vista
            "hideImages"  para esconder el contenido
            "showProgress" vamos hacer
    Llamamos al interactuador con interactor.execute();


                @Override
                public void getImageTweets() {
                    if (this.view != null){
                        view.hideElements();
                        view.showProgress();
                    }
                    this.interactor.execute();//this.interactor.getImageItemsList();
                }
    Lo opuesto en "onEventMainThread" entonces aquí, este método va a ser el que se subscribe
            mostramos "showImages"
            escondemos "hideProgress"

            @Override
            @Subscribe
            public void onEventMainThread(ImagesEvent event) {
                String errorMsg = event.getError();
                if (this.view != null) {
                    view.showElements();
                    view.hideProgress();
                    if (errorMsg != null) {
                        this.view.onError(errorMsg);
                    } else {
                        this.view.setContent(event.getImages());
                    }
                }
            }

    Algo para hacer incapié aca, debemos tomar en cuenta que este método va a ser el que subscribe  por eso "@Subscribe" y al recibir algo
    tengo que validar si acaso hay o no hay un error y si lo hay
    lo voy a mostrar entonces vamos hacer "String errorMsg = event.getError" entonces mostramos
    las imágenes ocultamos el "progressBar" y luego verificamos si el mensaje de error es
    diferente de "null" quiere decir que ocurrió un error entonces vamos a decir "view.onError"
    y le enviamos el "errorMsg" y de lo contrario "view.setContent" y hacemos un "event.getImages"
    todo esto va dentro de la validación



    Tengo mi presentador y mi evento implementados y ahora puedo proceder al interactuador

    El interactuador, vamos a crear su implementación, va ser una clase "ImagesInteractorImpl" e implementa al contrato
    que ya tenía definido previamente "ImagesInteractor" quiere decir que voy a estar obligado a tener un
    método "execute" y aquí vamos a declarar un "ImagesRepository" que en el constructor
    voy a recibir

        ImagesRepository repository;

        public ImagesInteractorImpl(ImagesRepository repository) {
             this.repository = repository;
        }


    Al ejecutar voy a decir "repository.getImages"
        @Override
        public void execute() {
            repository.getImages();
        }

    Por ultimo tengo que tener un "RepositoryImplementation"
    este es una clase e implementa "ImagesRepository" listo, entonces en "getImages" es donde voy
    a colocar todo el contenido y aquí voy a necesitar un par de cosas cuando vayamos a
    implementar el repositorio.
        @Override
        public void getImages() {

        }



Commit14 :ImagesIndexWithOutLinks

        Vamos a trabajar en nuestro Repositorio, "RepositoryImpl"
            Vamos a necesitar un "EventBus" para comunicar los mensajes me interesa la clase que yo implemente
            También necesito un  "CustomTwitterApiClient"

            Ambos los vamos a recibir en el constructor eventualmente
        pues voy a publicar algún evento cuando hay un error o no

                private EventBus eventBus;
                private CustomTwitterApiClient client;

                public ImagesRepositoryImpl(EventBus eventBus, CustomTwitterApiClient client) {
                    this.eventBus = eventBus;
                    this.client = client;
                }

        Usualmente me apoyo en métodos auxiliares solo "post" y uno va recibir un error de hecho hagamos que reciba "List items, String error"
        Entonces aquí vamos a instanciar un "ImagesEvent event = new ImagenEvent" y asignarle los valores "event.setError(error)"
        "event.setImages(items)" "eventBus.post(event)"



        Para que sea más fácil, vamos a crear un
        método auxiliar que únicamente recibe el error entonces llama a este método previamente
        definido enviándole "null" en el primer parámetro
                private void postError(String error) {
                    postEvent(null,error);
                }

        Otro que únicamente recibe el listado de
        "items" y este envía los "items" y "null" en el caso de error,

                private void postImages(List<Image> items) {
                    postEvent(items,null);
                }

        Con esto podemos definir nuestro método que publicará el evento

            private void postEvent(List<Image> items,String error){
                ImagesEvent event = new ImagesEvent();
                event.setImages(items);
                eventBus.post(event);
            }

        listo puedo ahora implementar
        "get Image" para esto vamos a definir una constante de cuantos Tweets voy ir a traer
        inicialmente la voy a definir en 100

                private final static int TWEET_COUNT = 100;

        Vamos hacer un método de ayuda "private boolean  containsImages" y recibe un "Tweet tweet" entonces con este método voy a devolver un
        booleano validando si acaso el tweet tiene imágenes, entonces para eso revisamos si
        acaso contiene "entities" además estos "entities" tienen "media" y es diferente de "null" para decir que contiene y además "tweet.entities.media"
        podría estar vacío, yo no quiero que este vacío, entonces si todo esto se cumple, entonces
        el tweet contiene imágenes, y las puedo ir a traer, de lo contrario no me va interesar

                private boolean containsImages(Tweet tweet) {
                    return  tweet.entities != null && //tiene entities
                            tweet.entities.media != null && //estos entities tienen media(fotos)
                            !tweet.entities.media.isEmpty();//y estos media no son vacías
                }


        En el método getImages del contrato de Repository,

        Vamos hacer un "Callback" de "list callback = new Callback"
        entonces tengo que importar la clase para que me de sugerencias, entonces lo importamos
        y necesito un "callback" de una lita de Tweets  implemento los métodos que son dos, "success"
        y "failure" y en la parte de abajo voy a decir "Client.getTimeLineService" y le envío los
        parámetros que tengo voy a tener el "TWEET_COUNT, true, true, true, " "true" más y "callback"
        este método es definido por retrofit como le mandamos llamando a "getTimeLineService().homeTimeLine()"

                public void getImages() {
                    Callback<List<Tweet>> callback=new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> result) {
                           //...
                        }

                        @Override
                        public void failure(TwitterException exception) {
                           //...
                        }
                    };

                    client.getTimeLineService().homeTimeline(TWEET_COUNT,true,true,true,true,callback);
                }


        Implemento el método "success" y "failure" veamos si todo falla entonces es fácil porque a un "post" le envío "e.getLocalizerMessage"

                failure
                        postError(exception.getLocalizedMessage());

        Nada mas ahora si tengo éxito entonces voy a tener una respuesta y esta respuesta va
        incluir un listado de "tweets",lo que voy hacer es crear
        un listado de imágenes le llamo a este "items" este listado va estar vacío inicialmente y voy a revisar
        todo lo que recibimos le digo "for (Tweet tweet)" que va estar dentro del result.data,
        recuerden que lo que recibe es un "result" que contienen un "list" de "tweets" entonces
        vamos a ir revisando "if (containsImages(tweet))" si el elemento actual contiene imágenes vamos
        a hacer algo de lo contrario no

        Aquí instanciamos un modelo con la imagen entonces vamos ir
            colocando los valores "tweetModel.setId" le hacemos con "tweet.idStr"
            luego "tweetModel.setfavoriteCount(tweet.favoriteCount)"
            luego vamos a tomar el texto "tweetText" inicialmente va ser "tweet.text" pero como va traer un link yo no quisiera que eso vaya incluido entonces vamos a decir "int index = tweetText.indexOf("http")"
        entonces esto lo vamos a quitar si acaso lo incluyo ósea si el índice está incluido
        entonces "tweetText = tweetText.substring" desde "0" hasta que aparezca ese "index" entonces
        voy a ignorar el resto, asigno con un "setText" con esta variable
        y por ultimo necesitamos la foto, entonces voy a tener un "mediaEntity currentPhoto"
        que va ser "tweet.entities.media.get(0)" puedo tener la primera únicamente, recuerden que
        en el método "containsImagen" yo ya estoy validando que "tweet.entities" sea diferente
        de "null" que "tweet.entities.media" sea diferente de "null" de tal forma que pueda hacer esta
        llamada sin preocuparme por la validación, entonces vamos a ir a traer el "imageUrl = currentPhoto.get.mediaUrl"
        y lo agregamos "tweetModel.setImagenURL" con esta variable por ultimo ya que todo está
        listo entonces lo agregamos "items.add" con esto tengo listo todo los elementos que fui
        a traer
            success Method

                        List<Image> items=new ArrayList<>();
                        for (Tweet tweet:result.data){
                            if (containsImages(tweet)){
                                Image tweetModel = new Image();
                                tweetModel.setId(tweet.idStr);
                                tweetModel.setFavoriteCount(tweet.favoriteCount);

                                //esta parte lo podemos omitar, pero va si queremos quitar los enlaces dentro del texto
                                String tweetText=tweet.text;//asignamos el texto
                                int index=tweetText.indexOf("http");//veo si hay un valor de retorno para mi índice
                                if (index>0){//si acaso hay el índice entonces quito todo desde la priemra posición hasta que aparezca ese índice
                                    tweetText=tweetText.substring(0,index);
                                }
                                tweetModel.setTweetText(tweetText);

                                //este es para la foto, la piermoa únicamente(0), porque ya esta validado que tweetentites no este vacía
                                MediaEntity currentPhoto = tweet.entities.media.get(0);
                                String imageURL = currentPhoto.mediaUrl;
                                tweetModel.setImageURL(imageURL);
                                items.add(tweetModel);

                            }

        Sin embargo eso no quiere decir que estén ordenados, entonces ahora voy a proceder a ordenarlos lo voy a ordenar
        en base a la cantidad de favoritos "Collections.sort()" quiero ordenar unos "items" con un comparador
        de imágenes entonces lo que vamos a devolver aquí es
        que "t1.getFavoriteCount -image.getFavoriteCount" con eso realizamos la comparación del orden
        y estamos listos para publicar el evento, vamos a publicarlo únicamente si tiene algo
        de hecho vamos a publicarlo siempre porque es un listado que no va ser nulo, entonces
        le puedo decir "post(items)"
        Cabe recalcar que en este apartado puedo definir a mi Image(CustonEntity), como una clase que hereda de Comparator, y con este sobrecargar el método compareTo, y con eso ya tenemos definido nuestro comparador


                        Collections.sort(items, new Comparator<Image>() {
                        public int compare(Image t1, Image t2) {
                                    return t2.getFavoriteCount() - t1.getFavoriteCount();
                                }
                            });
                        postImages(items);


Commit15 :IDFragmentImages

        Inyección de Dependencias


        Vamos ahora implementar la inyección de dependencias creando un nuevo paquete llamado "di" dentro de "images" package
            images
               di
                ...

        Necesitamos algo muy parecido al caso anterior cuando usamos librerías como inyecciones
        necesitamos dos clases, de hecho una clase y una interfaz

            "ImagesModule" que es la clase
             la interfaz se llama "ImagesComponent"

        Usando la interfaz
        La interfaz lo que va proveer es una especie de "API"
        en donde se va usar esta inyección entonces tengo dos formas de manejarlo para empezar
        vamos a ponerle ciertas anotaciones
        Es un "Singleton" y va ser uso de unos módulos
        para definir el componente, entonces aquí voy a definir que módulos voy a estar utilizando
        voy a utilizar ciertos módulos previamente definidos por mí mismo, por lo menos el que
        voy a utilizar es "ImagesModule.class" pero además como voy a estar usando
        ciertas librerías, vamos a poner aquí también "Libs.Module.class"

                @Singleton
                @Component(modules = {ImagesModule.class,LibsModule.class})


        Luego para inyectar, exponer este "API" tengo dos opciones
            Una opción es llamar al método "inject" ponerle
            de nombre "inject" y especificar cuál es el "target" por ejemplo "imagesFragment" este
            es el target donde voy a inyectar

                    void inject(ImagesFragment imagesFragment);

            Una segunda opción sería devolver un objeto por ejemplo
            "ImagesPresenter" y ponerle algún nombre al método digamos "getPresenter" entonces
            en el módulo voy a crear los "provides" y este método lo puedo mandar a llamar en base
            a el componente, en este caso por el momento voy a dejar a ambos

                    ImagesPresenter getPresenter();


        Ahora voy a trabajar al Módulo "ImagesModule"

        Recordemos tiene que tener una anotación "module" y luego
        vamos a ejecutar aquí todos los "provides" que voy a inyectar.

                    @Module
                    public class ImagesModule {
                        ...
                    }

        Voy a inyectar un adaptador y todo el "stack" de la arquitectura
        el adaptador va usar un "ImageLoader" en algún momento uso un "eventBus" por eso necesito ver las librerías,
        pero hay ciertas cosas que no puedo inyectar, sino tengo que recibirlas en un constructor, en concreto lo que necesito
        es la vista y un "onItemCliclListener" básicamente eso sería lo que necesito en el constructor



                    public ImagesModule(ImagesView view, OnItemClickListener clickListener) {
                        this.view = view;
                        this.clickListener = clickListener;
                    }

        Tenemos que escribir métodos por cada cosa que vamos a proveer
        entonces vamos a proveer un "ImagesAdapter providesAdapter" entonces este método va
        a devolver un adaptador

                    @Provides
                    @Singleton
                    ImagesAdapter provideAdapter(List<Image> items,ImageLoader imageLoader, OnItemClickListener clickListener) {
                        return new ImagesAdapter(items,imageLoader, clickListener);
                    }


        necesito volver un "new imagesAdapter"
        que recibe como parámetros, vamos ir a revisar que no mas necesito dentro de mi ImagesAdapter, "ui" "Adapter" lo que tiene es un listado
        un "imageLoader" y un "OnItemClickListener" entonces esto lo que voy a recibir y con eso
        voy a construir mi adaptador, necesito entonces de algún lugar obtener este "dataset" este
        "imageLoader" y este "clickListener", el "imageLoader" lo está proveyendo la inyección de dependencias
        de la librería entonces si vamos a ver la carpeta "LibsModule" de "di" tengo un módulo
        que tiene un "provides imageLoader" entonces como en mi componente estoy especificando
        que voy a usar el módulo de las librerías lo que va ser "dagger" en tiempo
        de compilación es permitirme acceder a esto y ya tengo mi "imageLoader" sin embargo no
        tengo un "dataset" y no tengo un "ClickListener" entonces necesito un método que provea cada uno de estos, entonces
        vamos hacer un método "provides OnItemClickListener" que no recibe nada y lo que devuelve es el
        "ClickListener" que recibí en el constructor

                    @Provides
                    @Singleton
                    List<Image> provideItemsList() {
                        return new ArrayList<Image>();
                    }

                    @Provides
                    @Singleton
                    OnItemClickListener provideClickListener() {
                        return this.clickListener;
                    }


        Algo similar vamos hacer para proveer el listado, vamos a devolver este "List"
        le ponemos "providesItemList" vamos a ponerle podría ser una inyección con nombre si en
        algún caso estuviera proveyendo más de un listado y aquí lo que voy a devolver es un
        "new ArrayList" de "Image" vacío, podemos usar "@Name" para proveer mas de un listado, (dos objetos del mismo tipo pero son diferentes)



        Con esto acabamos la inyección del adaptador

        Necesito devolver todo lo demás, el objeto que voy a tener dentro de mi "target" que
        en este caso el "target" es el fragmento además del "adapter" es un "presenter" entonces necesito
        un "imagesPresenter" "provides ImagesPresenter"


        Si voy a ver el presentador vamos a ver el presentador entonces lo que el presentador
        recibe es un "imagesView" un "eventBus" y un "Interactor" entonces lo colocamos aquí
        y vamos a devolver un "imagesPresenterImplementation" a partir de estos parámetros el "eventBus"
        viene de la librería, entonces voy a necesitar escribir un "provides" para "View" y "Interactor"


                @Provides
                @Singleton
                ImagesPresenter provideImagesPresenter(EventBus eventBus,ImagesView view, ImagesInteractor interactor) {
                    return new ImagesPresenterImpl(eventBus, view, interactor);
                }
                //como  EventBus viene de la librería voy a necesitar un provides para ImageView
                @Provides
                @Singleton
                ImagesView provideImagesView() {
                    return this.view;
                }

                @Provides
                @Singleton
                ImagesInteractor provideImagesInteractor(ImagesRepository repository) {
                    return new ImagesInteractorImpl(repository);
                }

                @Provides
                @Singleton
                ImagesRepository provideImagesRepository(EventBus eventBus,CustomTwitterApiClient client) {
                    return new ImagesRepositoryImpl(eventBus,client);
                }

        Entonces necesito un  "provides ImagesView" va devolver el "View" que recibió en el constructor y
        "provides images interactor", "ImagesInteractor" va devolver
        un "ImagesInteractor" entonces voy a ver mi implementación del Interactuador y lo que
        recibe es un "ImagesRepository" entonces vamos a corregir aquí los parámetros colocamos
        el repositorio y listo, estamos devolviendo un repositorio


        Ahora de donde sale ese repositorio, entonces tengo que devolver
        un repositorio con un "provides ImagesRepository" y vamos a ir a ver el repositorio que recibe,
        el repositorio está recibiendo un "eventBus" y un "Client" entonces vamos a enviarle aquí
        un "eventBus" que viene de la librería y un "Client" que ya voy a ver de dónde lo
        puedo obtener entonces vamos a construir un "ImagesRepositoryImplementation" a partir
        de "eventBus" y de "Client"

                @Provides
                @Singleton
                CustomTwitterApiClient providesCustomTwitterApiClient(TwitterSession session){
                    return new CustomTwitterApiClient(session);
                }

        Necesito proveer la session de Twitter
        Vamos a colocar ese parámetro y devolvemos un "new CustomTwitterApiClient"
        a partir de esa sesión ahora necesito un "provides" para la sesión, entonces vamos
        a devolver un "session provides TwitterSession" le vamos a poner esta ya no va tener ningún
        parámetro porque la sesión la puedo tener de "TwitterCore.getInstance.getSessionManager().getActiveSession()"
        listo si no tengo una sesión activa esto va ser nulo, pero como solo tengo las imágenes
        detrás de "login" tengo que tener una sesión activa.
                @Provides
                @Singleton
                TwitterSession providesTwitterSession(){
                    return  TwitterCore.getInstance().getSessionManager().getActiveSession();
                }


Commit16
            :SetupInjection_Fragment_prueba

        Listo tengo mi inyección de dependencias implementada con quien está proveyendo las
        dependencias ahora me corresponde

        Me dirijo al "target", "ImagesFragment" que es donde lo voy a usar lo que vamos hacer aquí

        En onCreateView
            después del "bind()" hacemos un setupInjection "setupInjection"

        Vamos a decirle a este fragmento que use el inyecto de dependencias con las dependencias que acabamos de escribir, para usarlo debería
        de compilar vamos a darle "make" la idea es que "dagger" cuando se compila va generar
        ciertos objetos que me van a servir aquí en la inyección
        y esto lo puedo hacer de varias formas
            Puedo tener componente de forma explícita aquí en el fragmento


            En aplicación que implementa la inyección

                ...
                    private void setupInjection() {
                        DaggerImagesComponent
                                .builder()
                                .libsModule(new LibsModule(this))
                                .imagesModule(new ImagesModule(this, this))
                                .build()
                                .inject(this);
                    }
                ...



        En TwitterClientApp, Clase aplicación

                public ImagesComponent getImagesComponent(Fragment fragment, ImagesView view, OnItemClickListener listener){
                    return DaggerImagesComponent
                            .builder()
                            .libsModule(new LibsModule(fragment))
                            .imagesModule(new ImagesModule(view, listener))
                            .build();
                }


        En fragmentAplication, clase que implementa la inyección

                private void setupInject() {
                    TwitterClientApp app=(TwitterClientApp)getActivity().getApplication();
                    ImagesComponent imagesComponent=app.getImagesComponent(this,this,this);
            //        presenter=imagesComponent.getPresenter();
                    imagesComponent.inject(this);

                }


        EXPLICACIÓN DE LOS DOS MÉTODOS


            Lo puedo   hacer en mi "aplication class" en este caso vamos hacerlo en el "aplication class" y vamos
            a decirlo aquí "public ImagesComponent getImagesComponent" y aquí vamos a regresar un "DaggerImagesComponent.builder"
            y le tengo que especificar los parámetros que va tener
                Vamos a decirle que va usar un "LibsModule" vamos hacer aquí mismo un "getLibsModule" "getLibsModule" recuerden que recibe un fragmento entonces tengo que especificarle el fragmento esta clase está definida en LibsModule de Inyección
                Luego vamos hacer un ".imagesModule(new ImagesModule(Espera recibir))" esperar recibir también algo
                Y luego le ponemos "build" y eso va devolver el componente

            Ahora para poder construir el "LibsModule"
                necesito un fragmento
                necesito un "imagesView" para el "imagesModule"
                necesito un "OnItemClickListener"  también para el imagesModule

            Entonces al "LibsModule" le voy a enviar el fragmento
            y a "ImagesModule" le voy a enviar el "view" y el "ClickListener"

            Como es un módulo puedo instanciar simplemente "new LibsModule(Fragment)"


       "Tomemos en cuenta qeu solo para el segundo Método"
       En Clase ImagesFragment


        Ahora regreso al fragmento y aquí en el "setupInjection" aquí vamos
        a declarar un "TwitterClientApp" a partir de "getActivity().getApplication()" de hecho
        ya que tengo esto voy a definir un componente voy a definir el "ImagesComponent" y vamos a decir "app.getImagesComponent"
        le tengo que enviar los parámetros el fragmento, la vista y el "listener"
            En este caso es "this" tres veces   porque tengo una clase que es un fragmento que implementa las otras dos interfaces y

        Luego realizo la inyección aquí: es donde nosotros definimos dos métodos que hacían lo mismo en "ImagesComponent" que dependiendo de cómo
        este definido el componente puedo hacer una llamada "inject" o puedo hacer un "getPresenter"
        entonces por ejemplo podría ser "imagesComponent.getPresenter" y así asignar a los objetos que estoy obteniendo,
        solamente los objetos principales no todo la cascada de objetos sino me interesan particularmente
        dos , me interesa el adaptador y presentador entonces podría decir por ejemplo "presenter = imagesComponent.getPresenter" tendré que
        hacer otra llamada y agregar otro método para el adaptador, en vez de eso lo voy a
        dejar comentado y lo que voy hacer es "imagesComponent.inject()" y le envío el fragmento "this"

        Y a estos dos campos los voy a decorar con "@Inject" de tal forma que cuando mande a llamar este
        método esta anotación lo que va provocar que estos cambios se inyecten

                    @Inject
                    ImagesPresenter presenter;
                    @Inject
                    ImagesAdapter adapter;

        Nos hace falta llamar en

            onCreateView
                "presentar.getImageTweets"
                "setupRecyclerView"

            En setupRecyclerView
            le asignamos los valores adecuados al "RecyclerView", "RecyclerView.setLayoutManager" y hacemos un "new GridLayoutManager(getActivity, 2)"
            para que tenga dos columnas y luego "recyclerView.setAdapter adapter"

                    private void setupRecyclerView() {
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        recyclerView.setAdapter(adapter);
                    }


        Algo muy importante esto debe ser     llamado después de la inyección para que esté listo todo lo que la inyección está
        proveyendo en este caso es el "presentador" y el "adaptador" si no voy a tener un "null", y por lo tanto una Excepción NullPointerException
        porque todavía no tiene ningún valor asignado estas dos variables, el orden es muy importante

            ....//orden de nuestro onCreateView para evitar el NullPointerException
                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                             Bundle savedInstanceState) {
                        View view = inflater.inflate(R.layout.fragment_content, container, false);
                        ButterKnife.bind(this, view);
                        setupInject();
                        setupRecyclerView();
                        presenter.getImageTweets();
                        return view;
                    }
            ...



    > N0TA: Como ya hemos visto se ha realizado algunas actualizaciones por lo que no esta demás explicarlas

        Adapter_RecyclerView_CardView       Hemos visto que dentro del adaptador usamos un CardView, con esto solo mandamos a inflar el layout.xml del cardView, mas no del fragment
        Layouts                             No hemos tenido en cuenta que no necesitamos un fragment, específico de cada Fragment, ya que estamos inflando otros como contenedores
        Fabric                              ya no es necesario, por lo que usamos Twitter kit
        Retrofit                            usamos Retrofit 2.0, porl o que las llamadas se hacen de otra forma quitando los callbacks y las llamadas como asíncronas, ya que este cambia a un solo patrón de diseño


            Antes
                @GET("/1.1/statuses/home_timeline.json")
                void homeTimeline(@Query("count") Integer count,
                                  @Query("trim_user") Boolean trim_user,
                                  @Query("exclude_replies") Boolean exclude_replies,
                                  @Query("contributor_details") Boolean contributor_details,
                                  @Query("include_entities") Boolean include_entities,
                                  Callback<List<Tweet>> callback);

            Después
                @GET("/1.1/statuses/home_timeline.json")
                Call<List<Tweet>> homeTimeline (@Query("count") Integer count,
                                                @Query("trim_user") Boolean trim_user,
                                                @Query("exclude_replies") Boolean exclude_replies,
                                                @Query("contributor_details") Boolean contributor_details,
                                                @Query("include_entities") Boolean include_entities
                                                );

        Para la llamada dentro de nuestro client, ImagesRepositoryImpl, que es le método que usa nuestra lógica y la que se comunica con el Modelo cambiamos

            Antes
                    //el Callback que teníamos definido es uno que me traía de una Solicitud Rest a la API de TWITTER
                    client.getTimelineService().homeTimeline(TWEET_COUNT, true, true, true, true,callback)


            Después

                    client.getTimeLineService().homeTimeline(TWEET_COUNT,true,true,true,true).enqueue(callback);

    para mas información de Twitter podemos refererinos acá
    https://dev.twitter.com/twitterkit/android/upgrading                                                                            Fabric
        https://dev.twitter.com/twitterkit/android/access-rest-api
        https://stackoverflow.com/questions/44966286/java-lang-runtimeexception-no-retrofit-annotation-found-parameter-3            Retrofit




Commit17

        :LayoutFragmentHashtags

        Vamos ahora a construir el "Layout" para el manejo de "Hashtags" noten que cada uno de
        los elementos tiene un texto y abajo los "hashtags" que están mostrados como una especie de listado
        bajo cada elemento
        A diferencia a las imágenes en donde tenía dos columnas y en cada elemento
        un "ImagesView" y un "TextView" ahora con el "HashTags" voy a tener un listado va lineal
        y dentro de cada uno voy a tener un "TextView" y un "RecyclerView" anidado, para esto tenemos
        que hacer unos ajustes en código que vamos a ver, pero empezamos con el "Layout"


        Vamos a usar una referencia "ContentImages.xml" pero lo voy
        a llamar ahora "ContentHashtags.xml" y vamos a conservar el "TextView" quitamos el "ImageView"
        y al "TextView" le vamos a llamar "txtTweet" le dejamos igual le vamos a agregar que tenga
        cierto tamaño, "Android:textAppearence" y la forma en que lo vamos a manejar es con
        "android:attr/textAppearanceMedium" para que tenga un tamaño mediado, no necesito un margen

        Además voy a tener un "RecyclerView" este "RecyclerView"
        el ancho va ser "match_parent" el alto va ser "wrap_content" y va tener un identificador,
        este identificador va ser "RecyclerView"
        En conclusión va tener un texto y por debajo del listado, este listado lo vamos a poner, como con columnas,
        pero lo vamos hacer en código, ósea el elemento de "ui" simplemente poner el render un listado,
        entonces con esto tenemos listo nuestro "Layout" para trabajar los "hashtags"


        En content_hashtags.xml
             <RelativeLayout
                    android:layout_width="match_parent"
                    android:layout_height="match_parent">

                    <TextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:textAppearance="?android:attr/textAppearanceMedium"
                        android:id="@+id/txtTweet"
                        android:layout_centerHorizontal="true" />

                    <android.support.v7.widget.RecyclerView
                        android:id="@+id/recyclerViewHashtags"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_below="@+id/txtTweet" />

                </RelativeLayout>

        Me hace falta agregar, que tenga un "Layout" especifico, por cada fila de este
        "RecyclerView" entonces vamos hacer aquí un nuevo "Layout" que se llame "row_hashtag_text.xml"
        y aquí vamos agregar un "TextView", vamos a ver el "TextView" que necesito es un "Plain Text View"
        tenemos de ancho y alto, lo que si le voy a poner es un identificador "txthashtag" y
        vamos agregar aquí, "TextColor" que color que vamos a usar, es veamos "android:textColor="@android:color/hole_blue_dark"
        y además vamos a poner un "background" el "background" le vamos a poner, de hecho como
        está mejor, si este es el "background" y el color entonces vamos a usar "@android:color/white" y le agregamos un pequeño
        "padding" para que no queden tan pegados a los bordes, con "@dimen/appbar_padding_top"
        entonces son 8dps queda aquí un poco mejor, esto es lo que vamos a ver en cada uno de
        los elementos de la fila, ahora estamos listos para pasar a la implementación.



        En row_hashtag_text.xml

                <TextView
                    android:padding="@dimen/appbar_padding_top"
                    android:background="@android:color/holo_blue_dark"
                    android:textColor="@android:color/white"
                    android:layout_width="wrap_content"
                    android:text="@string/app_name"
                    android:layout_height="wrap_content"
                    android:id="@+id/txtHashtag" />


Commit18
        :HashtagsMVP

        Para hacer un poco de práctica de lo que hemos aprendido hasta ahora, vamos a realizar los mismo que hicimos para ImagesMVP, aplicado a HashtagsMVP

        1.- "entity Hashtag"Primero definimos nuestra entidad a usar "Hashtag", es similiar a "Images" entity, pero aumentamos cosas como
                List<String> hashtags; que me ayuda a traer mi hashtags asociados, y eliminamos  "String imageUrl"
                Obteniendo así estos atributos dentro de mi hashtags

                        private String id;
                        private String tweetText;
                        private int favoriteCount;
                        private List<String> hashtags;//atributo añadido en vez de ImageUrl, el cual representa los hashtags asociados al Tweet, es el único cambio en el modelo
                        private final static String BASE_TWEET_URL = "https://twitter.com/null/status/";


        2.- "interface HashtagsView"Definimos a la vista, que teníamos como ImagesView, este va a ser HashtagsView
                Como en las imágenes pues teníanmos metodos que mostraban y ocultaban elementos,(imágenes), y métodos para ocultar y mostrar el progressBar
                También para notificar algún error al cargarse, por último un método setContent que llama a un List de Hashtags

                Obteniendo así estos métodos, que HashtagsPresenterImpl creará a su debido tiempo, ya sea para mostrar o solo para ocultar, algo a tener en cuenta es que
                mostramos en el onEventMainThread creado para eventBus, en el cual nos subscribimos
                ocultamos cuando estamos usando getImagesTweets.

                void showElements();
                void hideElements();
                void showProgress();
                void hideProgress();

                void onError(String error);
                void setContent(List<Hashtag> items);//este ayuda a enviar los hashtgas dentro del adapter para que se carguen dentro del recyclerView


        3.- "interface HashtagPresenter", en este paso definiremos el presentador, que como ya sabemos es el intermediario entre la vista y modelo de datos
            dará inteligencia a la vista y este recibe las interfaces que se implementan en la vista y el modelo, en nuestro caso ya que combiamos algunas arquitecturas, pues con el Interactuador
            podemos encontrar mas explicación dentro del link https://www.imaginanet.com/blog/patron-mvp.html

                En este caso tenemos los mismos métodos para nuestro Subscripción de EventBus, pero además tenemos un Evento para este, y su respectivos hashtags a obtener

                    void onResume();
                    void onPause();
                    void onDestroy();
                    void getHashtagTweets();
                    void onEventMainThread(HashtagEvent event);

        4.- "interface HashtagsInteractor" , como  ya sabemos este hace intermeio entre el presentador y el modelo dejando a los respositorios con toda la lógica que se necesita
            y en nuestro método execute pues traerá lo que hemos definido para obtener, es decir este hace la llamada al API de twitter llamando al repositorio que nos ayuda a interactura

                    void execute();

        5.- "interface HashtagsRepository", es el que tienen el método de traer nuestros datos, pero ya implementados por loque definio que hará en este tema
            por lo que ahora tendrémos un getHashtags

                    void getHashtags();

    Para los evetos de EventBus
        6.- "class HashtagsEvent" tendrémos un listado de Hashtags que ayudarán a usar en nuestras clases que implementan este evento,(HashtagsPresenterImpl),y un error que ayudrá a observar si tuvimos algún error al traerlos

                    private String error;
                    private List<Hashtag> hashtags;

                    public String getError() {
                        return error;
                    }

                    public void setError(String error) {
                        this.error = error;
                    }

                    public List<Hashtag> getHashtags() {
                        return hashtags;
                    }

                    public void setHashtags(List<Hashtag> hashtags) {
                        this.hashtags = hashtags;
                    }


    Para implementaciones

        7.- "class HashtagsPresenterImpl", esta clase es una implementación de nuestro contrato de Presentador y el Interacctuador
            para aquello necesitaremos
                EventBus            para registrar nuestros eventos
                HashtagsView        para la vista de nuestros hashtags
                HashtagsInteractor  para nuestro conector con el modelo, repository

            aquí implementamos algunos métodos que nos servirán para la vista, EventBus, y el Interactor

                onResume            eventBus.register(this)         //cuando la vista esta visible nos registramos
                onPause             eventBus.unregister(this)       //cuando la vista se manda a pausa se quita el registro
                onDestroy           view=null                       //eliminamos el memory lick de la vista haciéndola nula

                getHashtagTweets
                                    if (this.view != null){         //primero verificamos si la vista es diferente de null, para
                                        view.hideElements();        //ocultar nuestros Elementos y
                                        view.showProgress();        //mostrar nuestro progressBar
                                    }
                                    this.interactor.execute();      //luego ejecutamos nuestro interactor, recordemos qeu este execute del interactor es el que obteine los hashtags(o imágenes según como este definido) desde el repository


                onEventMainThread                                   //método de subscriber para EventBus

                    String errorMsg=event.getError();               //obtengo mi error puede ser nulo cuando no lo tenga
                    if (this.view!=null){                           //volvemos a verificar nuestra vista diferente de null y
                        view.showElements();                        //mostramos los elementos que es lo que estabamos viendo dentro de hashtagsFragments
                        view.hideProgress();                        //ocultamos el progressBar
                        if (errorMsg != null) {                     //verificamos si el mensaje es diferente de null
                            this.view.onError(errorMsg);            //y le mandamos a la vista el error
                        } else {
                            this.view.setContent(event.getHashtags());  //y mandamos a la vista los hashtags encontrados

                        }
                    }


        8.- "class HashtagsInteractorImpl" aquí vamos a implementar el método execute definido dentro de "HashtagsInteractor"
            como vimos este Interactuador necesita
                HashtagsRepository                  repository      //para hacer la lógica dentro de "HashtagsRepositoryImpl"
                public HashtagsInteractorImpl       Constructor     //para usar el constructor que necesito

            Método Sobrecargado

                execute()               repository.getHashtags      //para traer los hashtags que necesito desde el repositorio

        9.- "class HashtagsRepositoryImpl" aquí vamos a hacer las llamadas a la API de twitter, con esto logramos un código desacoplado de cada librería y mucho mas escalable
            ...LO DEJAMOS PARA EL OTRO VIDEO





    Para adapters del RecyclerView
        100.-Crear una clase Adapter(RecyclerView), que hereda de RecyclerView.Adapter, <ClaseNombre.ViewHolder>, implementar métodos y crear la clase ViewHolder dentro que herede de ViewHolder
            y le damos la lógica que necesitamos para que se instancia, al llamarla le hacemos los métodos para que este use todo loq ue hemos definido

                public class HashtagsAdapter extends RecyclerView.Adapter<HashtagsAdapter.ViewHolder> {

                    @Override
                    public HashtagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return null;
                    }

                    @Override
                    public void onBindViewHolder(HashtagsAdapter.ViewHolder holder, int position) {

                    }

                    @Override
                    public int getItemCount() {

                    }

                    public class ViewHolder extends RecyclerView.ViewHolder {
                        ....
                    }
                }

        101.- Podemos necesitar controlar un click dentro de este, ya sea un click largo o un click sencillo por lo que usaremos una interfaz que ayude a ver que estamos haciendo
              Definimos una interfaz que se llamará onItemClickListenerHashtags para controlar el onItemClickListener de cada vista, no le ponemos solo onItemClickListener ya que se confundiría dentro de cada llamada con el ClickListener del Images

                    public interface OnItemClickListenerHashtags {
                        void onItemClick(Hashtag tweet);
                        //void onItemClickListener(Images tweet)
                        //void onItemClickLong(Images tweets)
                    }
              En este apartado podemos comunicar cualquier tipo de objeto al hacer un click ya sea una lista o colección
              o un objeto simple definido, ademas podemos ponerle un onItemClickLong para controlar los click largos
                    para llamaralos dentro de n uestro adapatador solo debemos controlar una vista dentro del ViewHolder

                   //para hashtags con click
                            public void setOnClickListener(final Hashtag hashtag, final OnItemClickListenerHashtags listenerHashtags){
                                view.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view) {
                                        listenerHashtags.onItemClick(hashtag);
                                    }
                                });
                            }
                    // para Imgae con click
                            public void setOnClickListener(final Image image, final OnItemClickListenerHashtags listenerHashtags){
                                        view.setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View view) {
                                                listenerHashtags.onItemClick(image);
                                            }
                                        });
                            }
                    // para longClick
                            public void setOnClickListener(final Hashtag hashtag, final OnItemClickListenerHashtags listenerHashtags){
                                        view.setOnLongClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View view) {
                                                listenerHashtags.onItemClick(hashtag);
                                            }
                                        });
                            }

        103.-Para Usar un RecyclerView Anidado usaremos el Commit19, así que dirijamonos para allá

        104.-para hacer la llamada podemos tener un GridLayoutManager, y un LinearLayoutManager de acuerdo a como sea nuestra opción de vista
                ParaGridLayout
                    private void setupRecyclerView() {
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                            recyclerView.setAdapter(adapter);
                    }
                Para LinearLayout
                    private void setupRecyclerView() {
                            recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
                            recyclerViewContacts.setAdapter(adapter);
                    }
    Para Inyección de dependencias "módulos de inyección", como recordamos necesitabamos 1 clase que es el módulo que provee las clases y dependencias, y 1 interfaz que provee los métodos inject o getClass, recordando también que todo lo que ponemos como inject necesitammos tener en el módulo
    Vemos que la inyección teníamos dos opciones una para usarla desde la clase que lo implementa,  u otra para usarla desde el Application, ya que esta da una instancia a toda la aplicación , en este caso usaremos la segunda opción

        7.- "class HashtagsModule"
            Para este módulo de inyección tenemos una diferencia, ya no vamos a mostrar ningún contenido, entonces no necesitamos un imageLoader si no unicamente un "dataset", y el "clickListener", tomemos en cuenta que lo usamos para
            ver que se va a mostrar dentro de la vista (Actividad, Fragment,...),en este caso:
                    para proveernos un Adapter que lo necesitamos como al dar un click hacemos algo en concreto por eso usamos un onItemClickListener
                    List<Hashtag> dataset               //lista de Tweets y su respectiva lista de Hashtags
                    OnItemClickListener listener        //evento que controla el click dentro de cada item


            En resumen necesitamos
                HashtagsView view,// una vista para trabajar sobre ella porque esta no se la puede inyectar
                OnItemClickListener listener // que me ayuda a manejar el click , igual no se lo puede inyectar es por eso que usamos dentro del constructor

                Esta clase nos proveerá de un Adapter, "HashtagAdapter" a través de una lista de Hashtags y un ClickListener
                no olvidarnos de colocarle dentro de sus respectivas anotaciones
                    @Provides
                    @Singleton

                Hasta ahora tenemos algo como esto:

                    private HashtagsView view;
                    private OnItemClickListenerHashtags listenerHashtags;

                    public HashtagsModule(HashtagsView view, OnItemClickListenerHashtags listenerHashtags) {
                        this.view = view;
                        this.listenerHashtags = listenerHashtags;
                    }


                    @Provides
                    @Singleton
                    HashtagsAdapter providesHashtagsAdapter(List<Hashtag> dataset, OnItemClickListenerHashtags listenerHashtags){
                        return new HashtagsAdapter(dataset,listenerHashtags);
                    }

                Ahora vemos que necesitamos algo que me provea de List<Hashtag>, un onItemClick, por lo que necesitamos una cascada de proviciones de estos objetos, quedando un grafo de la siguiente manera

                    HashtagsAdapter providesAdapter
                        "Depende"       List<Hashtag>,OnItemClickListener listener

                        List<Hashtag> providesItemsList
                        "retorna"        ArrayList<Hashtag>();
                        OnItemClickListenerHashtags providesOnItemClickListener
                        "retorna"        repository this.listenerHashtags

                    HashtagsPresenter provideImagesPresenter
                        "Depende"        EventBus eventBus, HashtagsView view, HashtagsInteractor interactor

                        HashtagsView provideHashtagsView
                        "retorna"            this.view
                        HashtagsInteractor providesHashtagsInteractor
                        "Depende"           HashtagsRepository repository
                        "retorna"           HashtagsInteractorImpl

                                    HashtagsRepository providesHashtagsRepository
                                    "Depende"       EventBus eventBus, CustomTwitterApiClient client
                                    "retorna"       HashtagsRepositoryImpl

                                        providesCustomTwitterApiClient
                                        "Depende"       TwitterSession session
                                        "retorna"       CustomTwitterApiClient(session)

                                        providesTwitterSession
                                        "retorna"       TwitterCore.getInstance().getSessionManager().getActiveSession();

        8.- "interface HashtagsComponent"
            En este apartado siempre debemos hacer las anotaciones
                @Singleton                                                      //dagger annotation
                @Component(modules = {HashtagsModule.class,LibsModule.class})   //aquí definimos todos los módulos que necesitamos para crear nuestra inyección

            Y definimos el inject dentro de la vista
                void inject(HashtagsFragment hashtagsFragment);

            Algo a tener en cuenta es que todo lo que definamos en esta interfaz debe de estar en la clase Modules

                void inject...      inyecta todas las clases que necesito
                ImagesPresenter     inyecta solo imagesPresenter no toda la cascada de independencias

        9.- "Para implementar estas independencias"
            Para usar estas independencias debemos instanciar ya sea en la clase que necesita de la implementación, o en la clase aplicación
            a.- 1 Manera, con la inyección dentro de cada clase
                private void setupInjection() {
                        DaggerHashtagsComponent
                                .builder()
                                .libsModule(new LibsModule(this))
                                .twitterAppModule(new TwitterAppModule(getContext()))
                                .hashtagsModule(new HashtagsModule(this, this))
                                .build()
                                .inject(this);
                    }
            b.- 2 Manera, con inyección dentro de la APP
                Class que extiende de AplicationClass
                public HashtagsComponent getHashtagsComponent(HashtagsView view, OnItemClickListenerHashtags listenerHashtags){
                    return DaggerHashtagsComponent
                                .builder()
                                .libsModule(new LibsModule())
                                .hashtagsModule(new HashtagsModule(view,listenerHashtags))
                                .build();
                    }

            Explicación: la clase generada por dagger hemos de  construila a partir de las librerías que ncesita, en este caso
            "libsModule", y "hashtagsModule", mandándole los parámetros para nuestro correcto ingreso de parámetros, pero este caso en LibsModule puede ir con null o Creamos un constructor sin parámetros
            ya que solo estamos usando la vista y el onClickListener, porque el Fragmento como recordamos lo usabamso para Glide
            ya que este era como Picasso para las imágenes y como no tenemos imágenes que queremos cargar no es necesario.

            Cabe recalcar que este es el motivo por el cual en HashtagsModule no teníamos una instancia de EventBus ya que este ya era proveído por "libsModule"

            NOTA: Algo para tener en cuenta es qeu primero debemos compilar, para que use esta clase "DaggerHashtagsComponent" y luego poder construirla

    Presentador,Interactuador,funcionan de manera similar pero en adaptadores para childAdapter y respositorios son diferentes


Commit19
        :Nested_RecyclerView(RecyclerView Anidado)

    Procedemos a hacer un Nested RecyclerView para el texto y los Hashtags que poseemos dentro de nuestra lógica

    En HashtagsAdapter vamos a comenzar cambiando el "ViewHolder"
    que antes recibía un "View", ahora además va recibir un contexto
    Este contexto me va servir para construir el "Layout Manager " de
    cada uno de los "RecyclerViews" que van a ir dentro del "ViewHolder "
        public class HashtagsAdapter extends RecyclerView.Adapter<HashtagsAdapter.ViewHolder> {
         ...
            public class ViewHolder extends RecyclerView.ViewHolder {
            ...
            public ViewHolder(View itemView, Context context) {
            ...
         ...

    entonces voy a necesitar también agregar aquí un "Bind" hacia "RecyclerView"y el texto definido en el layout del content_hashtags
    Entonces dentro del "ViewHolder"
        dentro del constructor del "ViewHolder" voy a tener que, hacer un método o recibir en el constructor para colocar los "Items"

    así como tenía un "Bind" anterior ahora puedo recibir en el constructor o hacerlo en él,
    un método, ¿qué vamos hacer? vamos agregar aquí además de la vista "View view" un "private HashtagListAdapter"
    que este únicamente va referenciar a texto, entonces va ser un adaptador relativamente
    simple, pero un adapatador con sus respectivas clases a implementar

    Creamos el HashtagsListAdapter definiendo
            private List<String> items;     // un listado de elementos, son "Strings" le vamos a llamar "Items" y por supuesto vamos

            ....
            //creamos la vista de acuerdo a el layout definido como row_hashtag_text
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hashtag_text, parent, false);

        En ViewHolder
            //Hacemos el "bind" hacia text de nuestro layout en "row_hashtags_text"
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hashtag_text, parent, false);

        En onBindViewHolder
            //Lo que va ser es "holder.txthashtag.setText" en base "Items.get(position)"
            holder.txtHashtag.setText(items.get(position));

    Y los otros métodos que por lógica ya lo tenemos definidos


    En HashtagsAdapter
        En ViewHolder de este Adaptador

            Voy a ser uso de este adaptador dentro del "ViewHolder", aquí, entonces tengo definido el adaptador, aquí defino, después de la
            1.-vista, definimos, los "items" como un "ArraysList"
            2.-vamos a construir nuestro adaptador en base a la variable de items(dentro del constructor)
            3.-Necesito un "LayoutManager" específico para el "RecyclerView" colocando
                    un "setLayoutManager"           //por el momento "null"
                    un "setAdapter(adapter)"        //para definir el adaptador de HashtagListAdapter


        public class ViewHolder extends RecyclerView.ViewHolder {
        ...
        private View view;
        private HashtagListAdapter adapter;
        private ArrayList<String> items;
        public ViewHolder(View itemView, Context context) {
           ...

            items = new ArrayList<String>();
            adapter= new HashtagListAdapter(items);

            CustomGridLayoutManager layoutManager=new CustomGridLayoutManager(context,3);
            recyclerViewHashtags.setLayoutManager(null);
            recyclerViewHashtags.setAdapter(adapter);

        }

    Creamos el método "public void setItems" (fuera del Constructor) con los "Items" necesarios para mostrar
        1.- Como voy a reciclar vistas, previo a mostrarlo voy hacer un "clear"
        2.- Voy a agregar un "addAll" los nuevos elementos
        3.- Por último le aviso, al adaptador que cambiaron los datos,

                    public void setItems(List<String> newItems){
                        items.clear();
                        items.addAll(newItems);
                        adapter.notifyDataSetChanged();
                    }

    Procedemos a crear un LayoutManager personalizado con la la idea, de que, no tenga problema a la hora de anidar "ReciclerViews"
    es muy parecido al "GridLayoutManager" pero va a tener ciertos métodos implementados, lo encontramos el código en StackOverFlow link(https://stackoverflow.com/questions/26649406/nested-recycler-view-height-doesnt-wrap-its-content)
    lo único que cambiamos es que estaba para un  "LinearLayout" y ahora está para un "GridLayout"
    Le vamos a llamar "CustomGridLayoutManager" esto va requerir
            1.-un contexto, por eso es que necesito un contexto dentro del Constructor de este ViewHolder,
            2.-la cantidad de columnas que quiero mostrar

                CustomGridLayoutManager layoutManager=new CustomGridLayoutManager(context,3);



    Regreasamos a HashtagsAdapter


        "ViewHolder"
            porque necesitamos enviar un contexto además de la vista esto lo voy hacer a través de "parent.getContext"
            recordemos que ese "parent" es la vista de donde estamos mostrando el listado, la grilla, el "Recycler"

                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_hashtags, parent, false);
                    return new ViewHolder(view,parent.getContext());
                }

        "onBindViewHolder"
            lo que, lo que se va modificar es que, además del "setText" y el "ClickOnListener" vamos a decir "holder.setItems" y estos "Items"
            lo vamos a obtener de este "tweet" le ponemos "getHashtags", con esto ya estoy obteniendo
            el listado "Hashtags" que tiene guardado el objeto, la entidad que representa este modelo,
            básico de un "Hashtag" entoncesestos "hashtags" se los estoy enviando al "holder", el "holder" lo que hace es limpia
            los "items" que tenía antes, los agrega en este listado y le avisa al adaptador, noten
            que a diferencia de cómo lo venía manejando aquí no encerré la lógica, dentro del "hashtagsListAdapter"
            sino que esta fuera y es través de este listado de "Items" en donde yo mantengo un control
            de que voy a estar mostrando

                @Override
                public void onBindViewHolder(ViewHolder holder, int position) {
                    Hashtag tweet=dataset.get(position);
                    holder.setOnClickListener(tweet,listenerHashtags);
                    holder.txtTweet.setText(tweet.getTweetText());
                    holder.setItems(tweet.getHashtags());
                }






