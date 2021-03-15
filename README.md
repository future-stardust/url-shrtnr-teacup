# TeaCup - URL shortener 🍵

## Students group

- Молчанова Варвара kapibarabanka@gmail.com
- Музика Олександр lunosgoode@gmail.com
- Ткаченко Олександра mremarka314@gmail.com

## Design document

The [Tea design document] that
describes architecture and implementation details of this project.

## How to work with system
Follow the guidance provided in this [link](/UserGuide.md).

### System structure

After the third laboratory assignment groups will switch projects with one another. Because of this,
all projects have to have the same high-level structure. Also, this is the reason why you should not
modify project dependencies.

Please remember that the main goal of the course is **testing** and everything else is just an 
implementation harness.

There are four modules:
- `auth` **authentication module** - creates new users, authenticates existing ones
- `dataservice` - **data service** - a key-value persistence storage. There's a core folder `.data`. For each user a new folder created which contains file `<email>.usr` with json with emai and hash-password. Also in the core folder there's a file `ids.json` which contains 2 fields: `next id` and `available ids`. Files with alias is stored in users' folders in order to simplify searching through users' urls. 
- `urlservice` - **business logic** - logic of URL shortening
- `rest` - **REST API** - a module that provides a REST API. [Micronaut] framework is already added
  to project dependencies. It simplifies creation of REST API and provides built-in JWT 
  authentication.

## Environment prerequisites

### Java
This is a Java project, so you will need an environment with installed [JDK] 15. For installation, 
you could use:
- [sdkman] on Linux/MacOS 
- [AdoptOpenJDK] on Windows

### IDE  
As IDE use [IntelliJ Idea Edu].

### Checkstyle
We use [checkstyle] to ensure coding standards. To get real-time detection in IDE you could use [Checkstyle-IDEA] 
plugin. We use Google rules (local copy `./config/checkstyle/checkstyle.xml`).

## How to start development

1. Clone this repo
2. Open the project directory in IntelliJ Idea Edu
3. Configure IDE code style settings
  
    1. Open `Settings`
    2. Go to `Editor` -> `Code Style` -> `Import Scheme`
       ![Settings screenshot](./media/code-style-import.png)
    3. Import scheme from `./config/idea/intellij-java-google-style.xml`

## Commit messages

Write commit messages accordingly by [7 rules of good commit messages].

[Tea design document]: https://docs.google.com/document/d/1OPk6CApumRzTtpvQ1HzwoD5TqnTphwXwyOq6GzNVffE/edit?usp=sharing
[JDK]: https://en.wikipedia.org/wiki/Java_Development_Kit
[IntelliJ Idea Edu]: https://www.jetbrains.com/idea-edu/
[sdkman]: https://sdkman.io/
[AdoptOpenJDK]: https://adoptopenjdk.net/
[7 rules of good commit messages]: https://chris.beams.io/posts/git-commit/#seven-rules
[Micronaut]: https://micronaut.io/
[checkstyle]: https://checkstyle.org/
[Checkstyle-IDEA]: https://plugins.jetbrains.com/plugin/1065-checkstyle-idea
