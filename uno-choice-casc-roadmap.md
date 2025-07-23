# uno-choice Plugin – JCasC Compatibility Tracker  
*Last updated: **2025-07-23***

> Goal: add first‑class Jenkins Configuration‑as‑Code (JCasC) support to the **Active Choices / “uno‑choice”** plugin so parameters defined with this plugin can be configured purely from YAML.

---

## 1 Current Situation

| Item | Status |
|------|--------|
| Plugin version in prod | `<fill in>` |
| JCasC baseline | `2.0`+ (plugin fails to load) |
| Jira | [JENKINS‑75291](https://issues.jenkins.io/browse/JENKINS-75291) |
| blocker log snippet | `io.jenkins.plugins.casc.ConfiguratorException: Item isn't a Mapping` |

---

## 2 Design Decision

| Option | Do we pursue? | Rationale |
|--------|---------------|-----------|
| **A. Native Describable + DataBound**<br>• convert `Script`, `GroovyScript`, etc. | ☐ / ☐ | one‑time API change; no support plugin needed |
| **B. Configurator shim**<br>• write `ScriptConfigurator` | ☐ / ☐ | quickest PoC; zero API impact; can live in sidecar |

*Action:* tick one once a decision is made.

---

## 3 Task Breakdown

### 🚧 Core code changes (Option A)

- [ ] **Make `Script` describable**  
  - extend `AbstractDescribableImpl<Script>`  
  - add `@DataBoundConstructor`  
  - create `DescriptorImpl` with `@Extension` + `@Symbol("script")`
- [ ] **Repeat for subclasses** (`GroovyScript`, `ScriptlerScript`, etc.)
- [ ] **Annotate parameter classes** (`ChoiceParameter`, `CascadeChoiceParameter`, …) with `@DataBoundConstructor` / `@DataBoundSetter`
- [ ] **Expose getters** for all YAML‑bindable fields
- [ ] **Unit tests** – round‑trip YAML using `ConfiguratorRunner`
- [ ] **Bump POM** -> minimum Jenkins core & bom for CasC

### 🛠️  Configurator shim (Option B)

- [ ] Create `src/main/java/org/biouno/unochoice/casc/ScriptConfigurator.java`
- [ ] Map YAML ⇆ `Script` (see sample code)
- [ ] Register `@Extension @Symbol("script")`
- [ ] Add functional test with demo YAML
- [ ] Package as `uno-choice-casc-support.hpi`

### ✅ Common Steps

- [ ] Reproduce current failure with **minimal `jenkins.yaml`**  
  ```yaml
  parameters:
    - choiceParameter:
        name: ENV
        script: |
          return ["dev", "qa", "prod"]
  ```
- [ ] Clone & build plugin locally (`mvn hpi:run`)
- [ ] Open **draft PR** → link to JENKINS‑75291
- [ ] Request review from maintainers: @kinow, @biouno
- [ ] Update docs / README with JCasC examples
- [ ] Publish release notes

---

## 4 Timeline *(tentative)*

| Week | Milestone |
|------|-----------|
| W‑0  | Reproduce failure, commit failing test |
| W‑1  | Decide A vs B, scaffold code |
| W‑2  | Green tests locally |
| W‑3  | Draft PR, CI green |
| W‑4  | Maintainer feedback incorporated |
| W‑5  | Release to update center |

---

## 5 Reference Links

* Jira ticket: <https://issues.jenkins.io/browse/JENKINS-75291>  
* CloudBees KB: <https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/troubleshooting-guides/my-instance-is-failing-to-start-because-active-choice-plugin-is-not-casc-compatible>  
* Plugin repo: <https://github.com/biouno/uno-choice-plugin>  
* JCasC Dev Guide: <https://www.jenkins.io/doc/developer/extensions/jenkins-configuration-as-code/>  

---

*Feel free to add extra tasks, notes, or findings as you work.*
