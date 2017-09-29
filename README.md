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



Commit10
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





