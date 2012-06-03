package org.jnario.spec.jvmmodel;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.inject.Inject;
import java.util.Iterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend.core.xtend.XtendFunction;
import org.eclipse.xtend.core.xtend.XtendMember;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.jvmmodel.ExtendedJvmTypesBuilder;
import org.jnario.runner.Subject;
import org.jnario.spec.jvmmodel.Constants;
import org.jnario.spec.spec.ExampleGroup;
import org.jnario.spec.spec.TestFunction;

/**
 * @author Sebastian Benz - Initial contribution and API
 */
@SuppressWarnings("all")
public class ImplicitSubject {
  @Inject
  private ExtendedJvmTypesBuilder _extendedJvmTypesBuilder;
  
  public void addImplicitSubject(final JvmGenericType type, final ExampleGroup exampleGroup) {
    final JvmTypeReference targetType = this.resolveTargetType(exampleGroup);
    boolean _or = false;
    boolean _equals = ObjectExtensions.operator_equals(targetType, null);
    if (_equals) {
      _or = true;
    } else {
      boolean _eIsProxy = targetType.eIsProxy();
      _or = BooleanExtensions.operator_or(_equals, _eIsProxy);
    }
    if (_or) {
      return;
    }
    boolean _hasSubject = this.hasSubject(type);
    if (_hasSubject) {
      return;
    }
    boolean _neverUsesSubject = this.neverUsesSubject(exampleGroup);
    if (_neverUsesSubject) {
      return;
    }
    EList<JvmMember> _members = type.getMembers();
    final Procedure1<JvmField> _function = new Procedure1<JvmField>() {
        public void apply(final JvmField it) {
          EList<JvmAnnotationReference> _annotations = it.getAnnotations();
          JvmAnnotationReference _annotation = ImplicitSubject.this._extendedJvmTypesBuilder.toAnnotation(exampleGroup, Subject.class);
          ImplicitSubject.this._extendedJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
          it.setVisibility(JvmVisibility.PUBLIC);
        }
      };
    JvmField _field = this._extendedJvmTypesBuilder.toField(exampleGroup, Constants.SUBJECT_FIELD_NAME, targetType, _function);
    _members.add(0, _field);
  }
  
  public JvmTypeReference resolveTargetType(final ExampleGroup exampleGroup) {
    JvmTypeReference _targetType = exampleGroup.getTargetType();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_targetType, null);
    if (_notEquals) {
      JvmTypeReference _targetType_1 = exampleGroup.getTargetType();
      return this._extendedJvmTypesBuilder.cloneWithProxies(_targetType_1);
    }
    final ExampleGroup parentGroup = this.parent(exampleGroup);
    boolean _equals = ObjectExtensions.operator_equals(parentGroup, null);
    if (_equals) {
      return null;
    }
    return this.resolveTargetType(parentGroup);
  }
  
  public ExampleGroup parent(final ExampleGroup exampleGroup) {
    EObject _eContainer = exampleGroup.eContainer();
    ExampleGroup _containerOfType = EcoreUtil2.<ExampleGroup>getContainerOfType(_eContainer, ExampleGroup.class);
    return _containerOfType;
  }
  
  public boolean hasSubject(final JvmGenericType type) {
    EList<JvmMember> _members = type.getMembers();
    final Iterable<JvmField> fields = Iterables.<JvmField>filter(_members, JvmField.class);
    final Function1<JvmField,Boolean> _function = new Function1<JvmField,Boolean>() {
        public Boolean apply(final JvmField it) {
          String _simpleName = it.getSimpleName();
          boolean _equals = ObjectExtensions.operator_equals(_simpleName, Constants.SUBJECT_FIELD_NAME);
          return Boolean.valueOf(_equals);
        }
      };
    final JvmField subjectField = IterableExtensions.<JvmField>findFirst(fields, _function);
    boolean _notEquals = ObjectExtensions.operator_notEquals(subjectField, null);
    if (_notEquals) {
      return true;
    }
    JvmTypeReference _extendedClass = type.getExtendedClass();
    final JvmType extendedClass = _extendedClass==null?(JvmType)null:_extendedClass.getType();
    boolean _equals = ObjectExtensions.operator_equals(extendedClass, null);
    if (_equals) {
      return false;
    }
    return this.hasSubject(((JvmGenericType) extendedClass));
  }
  
  public boolean neverUsesSubject(final ExampleGroup exampleGroup) {
    Iterator<XFeatureCall> allFeatureCalls = Iterators.<XFeatureCall>emptyIterator();
    final EList<XtendMember> members = exampleGroup.getMembers();
    Iterable<XtendFunction> _filter = Iterables.<XtendFunction>filter(members, XtendFunction.class);
    for (final XtendFunction example : _filter) {
      TreeIterator<EObject> _eAllContents = example.eAllContents();
      UnmodifiableIterator<XFeatureCall> _filter_1 = Iterators.<XFeatureCall>filter(_eAllContents, XFeatureCall.class);
      Iterator<XFeatureCall> _concat = Iterators.<XFeatureCall>concat(allFeatureCalls, _filter_1);
      allFeatureCalls = _concat;
    }
    Iterable<TestFunction> _filter_2 = Iterables.<TestFunction>filter(members, TestFunction.class);
    for (final TestFunction example_1 : _filter_2) {
      TreeIterator<EObject> _eAllContents_1 = example_1.eAllContents();
      UnmodifiableIterator<XFeatureCall> _filter_3 = Iterators.<XFeatureCall>filter(_eAllContents_1, XFeatureCall.class);
      Iterator<XFeatureCall> _concat_1 = Iterators.<XFeatureCall>concat(allFeatureCalls, _filter_3);
      allFeatureCalls = _concat_1;
    }
    final Function1<XFeatureCall,Boolean> _function = new Function1<XFeatureCall,Boolean>() {
        public Boolean apply(final XFeatureCall it) {
          String _concreteSyntaxFeatureName = it.getConcreteSyntaxFeatureName();
          boolean _equals = ObjectExtensions.operator_equals(_concreteSyntaxFeatureName, Constants.SUBJECT_FIELD_NAME);
          return Boolean.valueOf(_equals);
        }
      };
    XFeatureCall _findFirst = IteratorExtensions.<XFeatureCall>findFirst(allFeatureCalls, _function);
    return ObjectExtensions.operator_equals(null, _findFirst);
  }
}
