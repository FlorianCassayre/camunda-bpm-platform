/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.dmn.engine.impl.handler;

import org.camunda.bpm.model.dmn.instance.LiteralExpression;
import org.camunda.bpm.model.dmn.instance.Text;
import org.camunda.commons.utils.StringUtil;
import org.camunda.dmn.engine.handler.DmnElementHandlerContext;
import org.camunda.dmn.engine.impl.DmnExpressionImpl;
import org.camunda.dmn.engine.impl.context.DmnScriptContextImpl;
import org.camunda.dmn.juel.JuelScriptEngineFactory;

public abstract class AbstractDmnLiteralExpressionHandler<E extends LiteralExpression, I extends DmnExpressionImpl> extends AbstractDmnElementHandler<E, I> {

  @SuppressWarnings("unchecked")
  protected I createElement(DmnElementHandlerContext context, E expression) {
    return (I) new DmnExpressionImpl();
  }

  protected void initElement(DmnElementHandlerContext context, E expression, I dmnExpression) {
    super.initElement(context, expression, dmnExpression);
    initExpressionLanguage(context, expression, dmnExpression);
    initExpression(context, expression, dmnExpression);
  }

  protected void initExpressionLanguage(DmnElementHandlerContext context, E expression, DmnExpressionImpl dmnExpression) {
    String expressionLanguage = expression.getExpressionLanguage();
    if (expressionLanguage != null) {
      dmnExpression.setExpressionLanguage(expressionLanguage.trim());
    }
  }

  protected void initExpression(DmnElementHandlerContext context, E expression, DmnExpressionImpl dmnExpression) {
    Text text = expression.getText();
    if (text != null) {
      String textContent = text.getTextContent();
      if (textContent != null && !textContent.trim().isEmpty()) {
        dmnExpression.setExpression(textContent.trim());
        postProcessExpressionText(context, expression, dmnExpression);
      }
    }
  }

  protected void postProcessExpressionText(DmnElementHandlerContext context, E expression, DmnExpressionImpl dmnExpression) {
    // do nothing
  }

  protected boolean hasJuelExpressionLanguage(DmnExpressionImpl dmnExpression) {
    String expressionLanguage = dmnExpression.getExpressionLanguage();
    if (expressionLanguage == null) {
      expressionLanguage = DmnScriptContextImpl.DEFAULT_SCRIPT_LANGUAGE;
    }

    return JuelScriptEngineFactory.NAME.equals(expressionLanguage);
  }

  protected boolean isExpression(DmnExpressionImpl dmnExpression) {
    return StringUtil.isExpression(dmnExpression.getExpression());
  }

}
