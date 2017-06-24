## Contribution Guidelines

1. Check if all test cases are passing
    
```
sbt clean compile test
``` 

2. Check if coverage is 90 % above

```
sbt clean coverage test
sbt coverageReport
sbt coverageAggregate
```

3. Ensure 0 scalastyle warnings

```
sbt scalastyle
```