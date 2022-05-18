using Microsoft.AspNetCore.Mvc;
using System;

namespace WedlancerAPI.Authorize
{
    public class AuthorizeMultiplePolicyAttribute : TypeFilterAttribute
    {
        public AuthorizeMultiplePolicyAttribute(string policies,bool isAnd=false) : base(typeof(AuthorizeMultiplePolicyFilter))
        {
            Arguments = new object[] { policies,isAnd };
        }
    }
}
