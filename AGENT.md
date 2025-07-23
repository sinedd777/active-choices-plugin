# Active Choices Plugin - Agent Guidelines

## Build/Test Commands
- **Build**: `mvn clean install` - Main Maven build (default goal: clean test install)
- **Test (Java)**: `mvn test` - Run Java unit tests  
- **Test (JS)**: `yarn test` or `npm test` - Run Jest tests
- **Single Test**: `mvn test -Dtest=ClassName` - Run specific Java test class
- **Frontend Build**: `yarn build` or `yarn prod` - Build JS/TS assets with webpack
- **Dev Build**: `yarn dev` - Development webpack build

## Architecture & Structure
- **Jenkins Plugin**: Java-based plugin (.hpi packaging) for parametrized freestyle jobs
- **Core Package**: `org.biouno.unochoice` - Main parameter types (Active, Reactive, Reference)
- **Frontend**: TypeScript/JS with webpack, jQuery integration for UI interactions
- **Tests**: Java tests in `src/test/java/`, JS tests in `src/test/js/` (Jest)
- **Resources**: Help files, CSS, JS assets in `src/main/webapp/`

## Code Style & Conventions
- **License**: MIT header required in all Java files
- **Java**: Standard Jenkins plugin conventions, interface-based design (UnoChoiceParameter marker)
- **Package**: `org.biouno.unochoice.*` namespace
- **Frontend**: TypeScript preferred, Babel transpilation, targets modern browsers (not IE11)
- **Testing**: JUnit for Java, Jest for JavaScript with jsdom environment
- **Dependencies**: Jenkins plugin parent v5.18, jQuery3 API, script-security integration
