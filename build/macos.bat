SET main_jar=WarcraftTools-2.1.0.3.jar
SET version=2.1.0.3
cd ..
jpackage --input target/ --main-jar %main_jar% --main-class de.haevn.Launcher --app-version "%version%" --name WarcraftTools --vendor Haevn --description "Utility tool collection for World of Warcraft(r)" --icon src/main/resources/werkzeugkasten.ico --type dmg --mac-package-name WarcraftTools