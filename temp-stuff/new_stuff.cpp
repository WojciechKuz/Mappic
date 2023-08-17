#include <iostream>

// https://www.vishalchovatiya.com/21-new-features-of-modern-cpp-to-use-in-your-project/

using uint = unsigned int;
using verylong = unsigned long long; // 1. type aliases C++11

verylong miljon  = 1'000'000;
verylong miliard = 1'000'000'000; // 2. digit separators since C++14

using vlong = verylong;
constexpr vlong operator"" _KB(vlong kilobytes) // 3. user defined literals
{
    /*
    It's something like my imagined feature of units. But my idea featured virtual units,
    which doesn't need to be assigned to certain value and prevents mixing different units
    ex. adding meters to kilograms.
    This one has? to be implemented and have assigned value.
    */
    return kilobytes * 1024;
}
// about const and constexpr later, but this ^ takes 0 run time cost thanks to constexpr.

class stuff {
    private:
    int m_var1 = 0;
    int m_var2 = 3;
    public:
    stuff(int var1, int var2): m_var1(var1), m_var2(var2) {}; // 4. uniform initialization & non-static member init.
    // ???
};

std::array<int, 5> a = {1, 2, 3, 4, 5}; // 5. std::initializer_list
std::vector<int> v = {1, 2, 3, 4, 5};

// 6.
auto pi = 3.14; // auto - deduced type
template <typename X, typename Y>
auto add(X x, Y y) -> decltype(x + y) // decltype
{
    return x + y;
    // a return type must be specified either explicitly, or using decltype.
}

// 7. smart pointers
// instead of:
std::unique_ptr<int> i_ptr1{new int{5}};
// use recommended:
auto i_ptr2 = std::make_unique<int>(5);
// or:
std::unique_ptr<int> i_ptr2 = std::make_unique<int>(5);

// 8. nullptr

// 9. STRONGLY-typed enums
enum class STATUS_t : uint32_t // provided type
{
    PASS = 0,
    FAIL,
    HUNG
};

// 10. Typecasting. Explicit type casting. Works like overloading type casting operations.
struct demo
{
    explicit operator bool() const { return true; }
};
demo d;
if (d);                             // OK calls demo::operator bool()
bool b_d = d;                       // error: cannot convert 'demo' to 'bool' in initialization
bool b_d = static_cast<bool>(d);    // OK, explicit conversion, you know what you are doing

// 11. Move std::move instead of copying values ex. in swap function, we can use move

// 12. Forwarding references
int x = 1115;
auto&& var = x; // this. universal references.
// allows move (see 11.) and perfect forwarding - reference can be either rvalue or lvalue
void f(auto&& t) {
  // ...
}
int x = 0;
f(0); // deduces as f(int&&)
f(x); // deduces as f(int&)

// 13. variadic template - '...' accepts 0 or more parameters
template <typename First, typename... Rest>
void print(const First &first, Rest &&... args)
{
    std::cout << first << std::endl;
    print(args...);
}

// 14. defaulted functions, ...
struct eemo
{
    eemo() = default;
};
eemo d;
// ...deleted functions
class cemo
{
    int m_x;
public:
    cemo(int x) : m_x(x){};
    cemo(const cemo &) = delete;
    cemo &operator=(const cemo &) = delete;
};
cemo obj1{123};
cemo obj2 = obj1; // error -- call to deleted copy constructor
obj2 = obj1;      // error -- operator= deleted

// 15. delegating constructors. Constructors can call other constructors with:
class bemo {
    int m_var;
    bemo(int var) : m_var(var) {}
    bemo() : bemo(0) {} // here
};

// 16. Lambda expressions - not worth naming, used in one place piece of code.
// example:
auto generator = [i = 0]() mutable { return ++i; }; // capture i from outside scope, no parameters, body - return ++i
cout << generator() << endl; // 1
cout << generator() << endl; // 2
cout << generator() << endl; // 3

// syntax:
[ capture list ] (parameters) -> return-type   // because it can capture values from higher scope, lambda expressions are closures
{   
    method definition
} 
// MORE ABOUT LAMBDAS: https://www.vishalchovatiya.com/learn-lambda-function-in-cpp-with-example/