package example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static class Holder {
		private int i;

		public Holder(int i) {
			this.setI(i);
		}

		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}
	}

	public static int increase(int i) {
		return i + 1;
	}

	public static int decrease(int i) {
		return i - 1;
	}

	// increase action return decrease action
	public static Runnable increase(Holder holder) {
		holder.setI(increase(holder.getI()));
		return () -> holder.setI(decrease(holder.getI()));
	}

	// test!
	public static void main(String[] args) {
		Holder holder = new Holder(1);
		System.out.println(holder.getI());

		List<Runnable> decreaseList = new ArrayList<>();

		// increase four times
		decreaseList.add(increase(holder));
		decreaseList.add(increase(holder));
		decreaseList.add(increase(holder));
		decreaseList.add(increase(holder));

		System.out.println(holder.getI());

		// pop last increase action
		Runnable lastOne = decreaseList.remove(decreaseList.size() - 1);
		lastOne.run();

		// check the result
		System.out.println(holder.getI());

		// revoke twice again
		Runnable secondOne = decreaseList.remove(decreaseList.size() - 1);
		Runnable thirdOne = decreaseList.remove(decreaseList.size() - 1);

		secondOne.run();
		thirdOne.run();

		System.out.println(holder.getI());
		
		Object newProxyInstance = Proxy.newProxyInstance(Main.class.getClassLoader(), new Class[] {TestInter.class}, new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if(method.getName().equals("calucated")) {
					return  4;
				}
				else if(method.getName().equals("getList")) {
					return Arrays.asList("555");
				}
				return 0;
			}
			
		});
		TestInter x = (TestInter)newProxyInstance;
		int calucated = x.calucated(3, 4);
		System.out.println(calucated);
		List<String> list = x.getList();
		System.out.println(list);
	}
	
	
	public static interface TestInter{
		
		public List<String> getList();
		public int calucated(int a, int b);
	}
	
   
	
	
}
