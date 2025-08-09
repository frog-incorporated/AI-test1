// Settings file for Hexo multi-module project
// Defines the project name and includes the various modules that make up the app

rootProject.name = "Hexo"

// Include all modules in the project
include(
    ":app",
    ":core",
    ":ml",
    ":tools",
    ":agent",
)