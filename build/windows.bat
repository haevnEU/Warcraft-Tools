@echo off
SET version=2.1.0.3

ECHO ============================================================
ECHO [STEP 1/5] Clean previous builds new version %version%
RMDIR release /S /Q
MKDIR release

ECHO ============================================================
ECHO [STEP 2/5] Compile
CD ..
CALL mvn clean compile assembly:single

ECHO "============================================================
ECHO "[STEP 3/5] Move jar file to build directory
CD build
MOVE ..\target\WarcraftTools-%version%-jar-with-dependencies.jar release\WarcraftTools-%version%.jar

ECHO ============================================================
ECHO [STEP 4/5] Start packaging
CALL jpackage --input release/ -d release/ --main-jar WarcraftTools-%version%.jar --main-class de.haevn.Launcher --app-version "%version%" --name WarcraftTools --vendor Haevn --description "Utility tool collection for World of Warcraft(r)" --icon icons/werkzeugkasten.ico --type msi --win-dir-chooser --win-menu --win-shortcut --win-per-user-install

ECHO ============================================================
ECHO [STEP 5/5] Done
WAIT