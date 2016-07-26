# Step-by-step mapper
A simple Java object-to-object mapping DSL

[ ![Download](https://api.bintray.com/packages/jihor/maven/mapper/images/download.svg) ](https://bintray.com/jihor/maven/mapper/_latestVersion)

### API
#### Converter
**Converter** is the main class for mapping definitions. It holds a pipeline of commands (or steps, hence the name 'step-by-step mapper'). The following commands are available:

* `initializeTarget()` - always the first command in pipeline, this is a special step for target object initialization. Initializer provided here will be used to initialize the target object when the `convert()` function is called without argument providing an instance of if (i.e. without a `Supplier<TargetType>` or an instance of `TargetType`)
 
    Attributes:
    - targetInitializer: `Supplier<TargetType>`

* `step()` - the basic work unit, a named transformation command

	Attributes:
    - name: `String` (must be unique among the commands in the pipeline)
    - transformation body: `BiConsumer<SourceType, TargetType>`
    
    Should an exception be thrown, the whole pipeline is aborted with faulted step and the exception reported.
    
    
* `step() with check` - same as simple step but with a check to be performed before the transformation 
 	
    Attributes: 
    - name: `String` (must be unique among the commands in the pipeline)
    - check body: `Function<SourceType, String>` (if the check returns anything but null or throws an exception, the step is considered faulted and the whole pipeline is aborted)
    - transformation body: `BiConsumer<SourceType, TargetType>`

* `switchCase().when()...[.when()...][.otherwise()...].endSwitch()` - conditional steps. These steps start a nested transformation pipelines which must be terminated by an `end()` command

    Attributes:
    
    for `switchCase()`:
    - name: `String` (must be unique among the commands in the pipeline)
    
    for `when()`:
    - condition: `Predicate<SourceType>`
    
    for `otherwise()` and `endSwitch()`: none

* `end()` - a command marking the end of pipeline

#### Registry
**ConverterRegistry** can hold converters for `[SourceType, TargetType]` pairs. See tests in `ru.jihor.mapper.tests.*registry*` packages for usage examples.

* **QueryableConverterRegistry** can search for available transformation (e.g. when trying to map an instance of `A` to instance of `B`, if no converter is present for `[A, B]` exact pair, it will search for the nearest neighbor with `[? super A, B]` class pair).
* **ConfigurableConverterRegistrysimpler** simply searches for the exact `[A, B]` pair

Any registry can also search for a named converter.
To be of any use, the registry must be populated with converters (see *Usage* section).

### Usage
#### Pure Java
###### Converter
In a pure Java environment, use the DelegatingConverter for easy implementation:

```
public class MyConverter extends DelegatingConverter<Source, Target> {

    @Override
    protected Converter<Source, Target> configureDelegate() {
        return Converter
                .<Source, Target>builder()
                .initializeTarget(SampleTarget::new)
                .step("Map data", (src, target) -> ...)
                .step("Map additional data", (src, target) -> ...)
                .switchCase("Check something in source object")
                	.when(src -> src.getSomeIndicator() == 0)
		                .step("Map data", (src, target) -> ...)
                        .step("Map additional data", (src, target) -> ...)
                		.end()
                	.when(src -> src.getSomeIndicator() == 42)
		                .step("Map data other way", (src, target) -> ...)
                        .step("Map additional data", (src, target) -> ...)
                		.end()                        
                	.otherwise()
                		.step("Map data third way", (src, target) -> ... )
                		.end()
                .endSwitch()
                .step("Map more data", (src, target) -> ...)
                .end()
                .build();
    }
```
###### Registry
Registry can be implemented as follows:
```
public class DemoConverterRegistry extends ConfigurableConverterRegistry {

    // We'll be calling this registry from multiple converters so 
    // let's implement it as a singleton even though it's not mandated by the superclass, 
    public final static DemoConverterRegistry instance = new DemoConverterRegistry(); 

    public static ConverterRegistry getInstance() {
        return instance;
    }

    private DemoConverterRegistry(){}

    @Override protected void configureRegistry(SimpleRegistry aRegistry) {
        aRegistry.add(new ClassPair<>(PersonA.class, PersonB.class), "personConverter", new PersonAToPersonBConverter());
        aRegistry.add(new ClassPair<>(CardA.class, CardB.class), "cardConverter", new CardAToCardBConverter());
        aRegistry.add(new ClassPair<>(LoanA.class, LoanB.class), "loanConverter", new LoanAToLoanBConverter());
    }
}
```
#### Spring
###### Converter
In Spring context, Converter is most easily defined with a plain builder method:	

```
@Bean
public Converter<Source, Target> simpleConverter() {
    return Converter
            .<Source, Target>builder()
            .initializeTarget(SampleTarget::new)
            .step("Map data", (src, target) -> ...)
            .step("Map additional data", (src, target) -> ...)
            .switchCase("Check something in source object")
            .when(src -> src.getSomeIndicator() == 0)
            .step("Map data", (src, target) -> ...)
                    .step("Map additional data", (src, target) -> ...)
                    .end()
            .when(src -> src.getSomeIndicator() == 42)
            .step("Map data other way", (src, target) -> ...)
                    .step("Map additional data", (src, target) -> ...)
                    .end()
            .otherwise()
            .step("Map data third way", (src, target) -> ... )
                    .end()
            .endSwitch()
            .step("Map more data", (src, target) -> ...)
            .end()
            .build();
}
```
###### Registry
Registry is also most easily defined as an anonymous class (it's a shame abstract classes can't be implemented as labdas ;) ):
```
@Bean
public QueryableConverterRegistry registry() {
    return new QueryableConverterRegistry() {
        @Override
        protected void configureRegistry(SimpleRegistry aRegistry) {
            aRegistry.add(new ClassPair<>(PersonA.class, PersonB.class), "personConverter", personConverter());
            aRegistry.add(new ClassPair<>(CardA.class, CardB.class), "cardConverter", cardConverter());
            aRegistry.add(new ClassPair<>(LoanA.class, LoanB.class), "loanConverter", loanConverter());
        }
    };
}
```