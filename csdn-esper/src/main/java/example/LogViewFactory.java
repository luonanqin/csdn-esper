package example;

import java.util.List;

import com.espertech.esper.client.EventType;
import com.espertech.esper.core.context.util.AgentInstanceViewFactoryChainContext;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.view.View;
import com.espertech.esper.view.ViewFactory;
import com.espertech.esper.view.ViewFactoryContext;
import com.espertech.esper.view.ViewFactorySupport;
import com.espertech.esper.view.ViewParameterException;

public class LogViewFactory extends ViewFactorySupport {

	public void setViewParameters(ViewFactoryContext viewFactoryContext, List<ExprNode> viewParameters) throws ViewParameterException {

	}

	public void attach(EventType parentEventType, StatementContext statementContext, ViewFactory optionalParentFactory, List<ViewFactory> parentViewFactories)
			throws ViewParameterException {

	}

	public View makeView(AgentInstanceViewFactoryChainContext agentInstanceViewFactoryContext) {
		return null;
	}

	public EventType getEventType() {
		return null;
	}

}
