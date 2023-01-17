package net.ukr.yougetit.autoconfigure;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.jupiter.api.Test;

class S3PropertiesTest {

  @Test
  @SuppressWarnings("squid:2699") // Successful execution qualifies as test success.
  void testGetterAndSetter() {
    ValidatorBuilder.create()
        .with(new GetterTester())
        .with(new SetterTester())
        .build()
        .validate(PojoClassFactory.getPojoClass(S3Properties.class));
  }

}
