using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using System.Linq;
using System.Threading.Tasks;

namespace WedlancerAPI.Authorize
{
    public class AuthorizeMultiplePolicyFilter : IAsyncAuthorizationFilter
    {
        private readonly IAuthorizationService _authorization;
        public string _policies { get; private set; }
        public bool IsAnd { get; private set; }

        public AuthorizeMultiplePolicyFilter(string policies,bool isAnd, IAuthorizationService authorization)
        {
            _policies = policies;
            IsAnd = isAnd;
            _authorization = authorization;
        }
        public async Task OnAuthorizationAsync(AuthorizationFilterContext context)
        {
            var policys = _policies.Split(",").ToList();

            if(IsAnd)
            {
                foreach (var policy in policys)
                {
                    var authorized = await _authorization.AuthorizeAsync(context.HttpContext.User, policy);
                    if (!authorized.Succeeded)
                    {
                        context.Result = new ForbidResult();
                        return;
                    }
                }
            }
            else
            {
                foreach (var policy in policys)
                {
                    var authorized = await _authorization.AuthorizeAsync(context.HttpContext.User, policy);
                    if (authorized.Succeeded)
                    {
                        return;
                    }

                }
                context.Result = new ForbidResult();
                return;
            }

        }
    }
}
